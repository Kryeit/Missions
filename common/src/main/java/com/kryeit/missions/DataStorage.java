package com.kryeit.missions;

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

    // TODO: Bug when there's less than 10 different mission types
    private static List<ConfigReader.MissionTypeConfig> shuffleWeighted(Collection<ConfigReader.MissionTypeConfig> missionTypeConfigs, int length) {
        List<ConfigReader.MissionTypeConfig> shuffled = new ArrayList<>(length);
        List<ConfigReader.MissionTypeConfig> remaining = new LinkedList<>(missionTypeConfigs);

        for (int i = 0; i < length; i++) {
            Predicate<ConfigReader.MissionTypeConfig> filter = missionTypeConfig ->
                    !missionTypeConfig.missionType().assignOnlyOnce() || !shuffled.contains(missionTypeConfig) || remaining.stream().allMatch(m -> m.missionType().assignOnlyOnce());

            double totalWeight = remaining.stream()
                    .mapToDouble(ConfigReader.MissionTypeConfig::weight)
                    .sum();
            double randomNumber = Math.random() * totalWeight;
            double weightSum = 0;

            for (int j = 0; j < remaining.size(); j++) {
                ConfigReader.MissionTypeConfig missionTypeConfig = remaining.get(j);
                weightSum += missionTypeConfig.weight();
                if (weightSum >= randomNumber && filter.test(missionTypeConfig)) {
                    shuffled.add(missionTypeConfig);
                    if (missionTypeConfig.missionType().assignOnlyOnce()) {
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


    public void reassignActiveMissions(Map<MissionType, ConfigReader.MissionTypeConfig> missions, UUID player) {
        ListTag list = getActiveMissionsTag(player);
        list.clear();

        for (ConfigReader.MissionTypeConfig missionTypeConfig : shuffleWeighted(missions.values(), 10)) {
            list.add(createActiveMissionTag(missionTypeConfig));
        }
    }

    private static CompoundTag createActiveMissionTag(ConfigReader.MissionTypeConfig missionTypeConfig) {
        CompoundTag tag = new CompoundTag();
        ConfigReader.SubMissionConfig subMission = Utils.getRandomEntry(missionTypeConfig.subMissions());
        ConfigReader.Reward reward = Utils.getRandomEntry(subMission.rewards());
        Map.Entry<String, Range> requiredItem = Utils.getRandomEntry(subMission.items().entrySet());

        tag.putString("item", requiredItem.getKey());
        tag.putBoolean("completed", false);
        tag.putString("mission_id", missionTypeConfig.missionType().id());
        tag.putInt("required_amount", requiredItem.getValue().getRandomValue());
        tag.putString("title", Utils.getRandomEntry(subMission.titles()));
        tag.putString("reward_item", reward.item());
        tag.putInt("reward_amount", reward.count().getRandomValue());

        return tag;
    }

    public ConfigReader.MissionTypeConfig reassignActiveMission(Map<MissionType, ConfigReader.MissionTypeConfig> missions, UUID player, int index) {
        ListTag list = getActiveMissionsTag(player);
        String idToReassign = list.getCompound(index).getString("mission_id");

        List<ConfigReader.MissionTypeConfig> assignedTypes = new ArrayList<>(missions.values());
        assignedTypes.removeIf(m -> m.missionType().id().equals(idToReassign));

        ConfigReader.MissionTypeConfig missionTypeConfig = shuffleWeighted(assignedTypes, 1).get(0);
        CompoundTag newTag = createActiveMissionTag(missionTypeConfig);
        list.set(index, newTag);
        return missionTypeConfig;
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
        private final String rewardItem;

        public int rewardAmount() {
            return rewardAmount;
        }

        public String rewardItem() {
            return rewardItem;
        }

        private final int rewardAmount;

        private ActiveMission(ResourceLocation item, boolean isCompleted, String missionID, int requiredAmount, String title, String rewardItem, int rewardAmount) {
            this.item = item;
            this.isCompleted = isCompleted;
            this.missionID = missionID;
            this.requiredAmount = requiredAmount;
            this.title = title;
            this.rewardItem = rewardItem;
            this.rewardAmount = rewardAmount;
        }

        private ActiveMission(CompoundTag tag) {
            this(new ResourceLocation(tag.getString("item")),
                    tag.getBoolean("completed"),
                    tag.getString("mission_id"),
                    tag.getInt("required_amount"),
                    tag.getString("title"),
                    tag.getString("reward_item"),
                    tag.getInt("reward_amount")
            );
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
                    rewardAmount,
                    rewardItem
            );
        }
    }
}
