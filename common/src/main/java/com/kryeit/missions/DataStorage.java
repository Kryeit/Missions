package com.kryeit.missions;

import com.kryeit.Missions;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.config.Range;
import com.kryeit.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class DataStorage implements AutoCloseable {
    private static final File FILE = new File("config/missions/mission_data.nbt");
    private final CompoundTag data;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DataStorage() {
        try {
            FILE.getParentFile().mkdirs();
            if (!FILE.exists()) {
                data = new CompoundTag();
                save();
            } else {
                data = NbtIo.readCompressed(FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getRandomEntry(Collection<T> collection) {
        int num = (int) (Math.random() * collection.size());
        for (T t : collection) if (--num < 0) return t;
        throw new AssertionError();
    }

    public void save() {
        try {
            NbtIo.writeCompressed(data, FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompoundTag getMissionData(String missionID, UUID player) {
        CompoundTag missionData = getOrCreateTag(data, "mission_data");
        CompoundTag missionIDData = getOrCreateTag(missionData, missionID);

        if (missionIDData.contains(player.toString())) {
            return missionIDData.getCompound(player.toString());
        } else {
            CompoundTag newTag = new CompoundTag();
            missionIDData.put(player.toString(), newTag);
            return newTag;
        }
    }

    private ListTag getActiveMissionsTag(UUID player) {
        String uuidString = player.toString();
        CompoundTag activeMissions = getOrCreateTag(data, "active_missions");
        return getOrCreateList(activeMissions, uuidString);
    }

    public List<ActiveMission> getActiveMissions(UUID player) {
        ListTag list = getActiveMissionsTag(player);

        List<ActiveMission> output = new ArrayList<>();
        for (Tag tag : list) {
            output.add(new ActiveMission((CompoundTag) tag));
        }
        return output;
    }

    public void setCompleted(UUID player, ResourceLocation item, String missionTypeID) {
        for (Tag tag : getActiveMissionsTag(player)) {
            CompoundTag compound = (CompoundTag) tag;
            if (new ResourceLocation(compound.getString("item")).equals(item) && compound.getString("mission_id").equals(missionTypeID) && !compound.getBoolean("completed")) {
                compound.putBoolean("completed", true);
                break;
            }
        }
    }

    private static List<ConfigReader.Mission> shuffleWeighted(Collection<ConfigReader.Mission> missions, int length) {
        List<ConfigReader.Mission> shuffled = new ArrayList<>(length);
        List<ConfigReader.Mission> remaining = new LinkedList<>(missions);

        for (int i = 0; i < length; i++) {
            Predicate<ConfigReader.Mission> filter = mission ->
                    !mission.missionType().assignOnlyOnce() || !shuffled.contains(mission) || remaining.stream().noneMatch(m -> !m.missionType().assignOnlyOnce());

            double totalWeight = remaining.stream()
                    .mapToDouble(ConfigReader.Mission::weight)
                    .sum();
            double randomNumber = Math.random() * totalWeight;
            double weightSum = 0;

            for (int j = 0; j < remaining.size(); j++) {
                ConfigReader.Mission mission = remaining.get(j);
                weightSum += mission.weight();
                if (weightSum >= randomNumber && filter.test(mission)) {
                    shuffled.add(mission);
                    if (mission.missionType().assignOnlyOnce()) {
                        remaining.remove(j);  // Remove the mission to prevent it from being selected again
                    }
                    break;
                }
            }

            // If no mission was selected because all were filtered out, select the first mission
            if (shuffled.isEmpty() && i == length - 1) {
                shuffled.add(remaining.get(0));
            }
        }

        return shuffled;
    }


    public void reassignActiveMissions(Map<MissionType, ConfigReader.Mission> missions, UUID player) {
        ListTag list = getActiveMissionsTag(player);
        list.clear();

        for (ConfigReader.Mission mission : shuffleWeighted(missions.values(), 10)) {
            list.add(createActiveMissionTag(mission));
        }
    }

    private static CompoundTag createActiveMissionTag(ConfigReader.Mission mission) {
        CompoundTag tag = new CompoundTag();

        Map.Entry<String, Range> requiredItem = getRandomEntry(mission.items().entrySet());

        tag.putString("item", requiredItem.getKey());
        tag.putBoolean("completed", false);
        tag.putString("mission_id", mission.missionType().id());
        tag.putInt("required_amount", requiredItem.getValue().getRandomValue());
        tag.putString("title", getRandomEntry(mission.titles()));
        return tag;
    }

    public ConfigReader.Mission reassignActiveMission(Map<MissionType, ConfigReader.Mission> missions, UUID player, int index) {
        ListTag list = getActiveMissionsTag(player);
        String idToReassign = list.getCompound(index).getString("mission_id");

        List<ConfigReader.Mission> assignedTypes = new ArrayList<>(missions.values());
        assignedTypes.removeIf(m -> m.missionType().id().equals(idToReassign));

        ConfigReader.Mission mission = shuffleWeighted(assignedTypes, 1).get(0);
        CompoundTag newTag = createActiveMissionTag(mission);
        list.set(index, newTag);
        return mission;
    }

    public int getReassignmentsSinceLastReset(UUID player) {
        CompoundTag tag = getOrCreateTag(data, "reassignments");
        return tag.getInt(player.toString());
    }

    public void incrementReassignmentsSinceLastReset(UUID player) {
        String uuid = player.toString();
        CompoundTag tag = getOrCreateTag(data, "reassignments");
        tag.putInt(uuid, tag.getInt(uuid) + 1);
    }

    public void resetReassignments(UUID player) {
        CompoundTag tag = getOrCreateTag(data, "reassignments");
        tag.putInt(player.toString(), 0);
    }

    public int getLastAssignedDay(UUID player) {
        return data.getCompound("last_assigned").getInt(player.toString());
    }

    public void setLastAssignedDay(UUID player) {
        setLastAssignedDay(player, Utils.getDay());
    }

    public void setLastAssignedDay(UUID player, int day) {
        CompoundTag lastAssigned = getOrCreateTag(data, "last_assigned");
        lastAssigned.putInt(player.toString(), day);
    }

    public void addReward(UUID player, String item, int amount) {
        CompoundTag rewards = getOrCreateTag(data, "rewards");
        CompoundTag playerRewards = getOrCreateTag(rewards, player.toString());
        playerRewards.putInt(item, playerRewards.getInt(item) + amount);
    }

    public Map<String, Integer> getUnclaimedRewards(UUID player) {
        CompoundTag rewards = getOrCreateTag(data, "rewards");

        Map<String, Integer> output = new HashMap<>();
        CompoundTag playerRewards = rewards.getCompound(player.toString());
        for (String key : playerRewards.getAllKeys()) {
            output.put(key, playerRewards.getInt(key));
        }
        return output;
    }

    /**
     * Claims the player's rewards. Does NOT move the rewards into the player's inventory!
     *
     * @param player The player whose rewards to claim
     */
    public void claimRewards(UUID player) {
        data.getCompound("rewards").remove(player.toString());
    }

    private static CompoundTag getOrCreateTag(CompoundTag tag, String key) {
        CompoundTag compound = tag.getCompound(key);
        if (!tag.contains(key)) tag.put(key, compound);
        return compound;
    }

    private static ListTag getOrCreateList(CompoundTag tag, String key) {
        ListTag list = tag.getList(key, Tag.TAG_COMPOUND);
        if (!tag.contains(key)) tag.put(key, list);
        return list;
    }

    @Override
    public void close() {
        save();
    }

    public static class ActiveMission {
        private final ResourceLocation item;
        private final boolean isCompleted;
        private final String missionID;
        private final int requiredAmount;
        private final String title;

        private ActiveMission(ResourceLocation item, boolean isCompleted, String missionID, int requiredAmount, String title) {
            this.item = item;
            this.isCompleted = isCompleted;
            this.missionID = missionID;
            this.requiredAmount = requiredAmount;
            this.title = title;
        }

        private ActiveMission(CompoundTag tag) {
            this(new ResourceLocation(tag.getString("item")),
                    tag.getBoolean("completed"),
                    tag.getString("mission_id"),
                    tag.getInt("required_amount"),
                    tag.getString("title"));
        }

        public int requiredAmount() {
            return requiredAmount;
        }

        public String missionID() {
            return missionID;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public ResourceLocation item() {
            return item;
        }

        public ClientsideActiveMission toClientMission(UUID player) {
            MissionType type = MissionTypeRegistry.INSTANCE.getType(missionID());
            ConfigReader.Mission configMission = Missions.getConfig().getMissions().get(type);

            if (configMission == null) {
                List<ActiveMission> activeMissions = MissionManager.getActiveMissions(player);
                int index = -1;

                for (int i = 0; i < activeMissions.size(); i++) {
                    if (activeMissions.get(i).missionID().equals(missionID())) {
                        index = i;
                        break;
                    }
                }

                configMission = MissionManager.getStorage().reassignActiveMission(Missions.getConfig().getMissions(), player, index);
            }

            return new ClientsideActiveMission(
                    Component.nullToEmpty(title),
                    type.difficulty(),
                    requiredAmount(),
                    missionID(),
                    type.getProgress(player, item()),
                    type.getPreviewStack(item()),
                    type.getItemStack(item()),
                    type.description(),
                    isCompleted(),
                    configMission.rewardAmount(),
                    configMission.rewardItem()
            );
        }
    }
}
