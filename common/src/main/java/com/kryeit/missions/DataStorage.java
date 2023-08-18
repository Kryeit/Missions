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

public class DataStorage {
    public static final File FILE = new File("missions/mission_data.nbt");
    public static final DataStorage INSTANCE = new DataStorage();
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
            CompoundTag compound = (CompoundTag) tag;
            ResourceLocation item = new ResourceLocation(compound.getString("item"));
            boolean isCompleted = compound.getBoolean("completed");
            String missionID = compound.getString("mission_id");
            int requiredAmount = compound.getInt("required_amount");
            String title = compound.getString("title");
            output.add(new ActiveMission(item, isCompleted, missionID, requiredAmount, title));
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

    /*
    This method should not be used. Use MissionManager#reassignMissions(UUID player) instead
     */
    public void reassignActiveMissions(Map<MissionType, ConfigReader.Mission> missions, UUID player) {
        ListTag list = getActiveMissionsTag(player);
        list.clear();

        for (int i = 0; i < 10; i++) {
            CompoundTag tag = new CompoundTag();
            ConfigReader.Mission randomEntry = getRandomEntry(missions.values());
            Map.Entry<String, Range> item = getRandomEntry(randomEntry.items().entrySet());

            tag.putString("item", item.getKey());
            tag.putBoolean("completed", false);
            tag.putString("mission_id", randomEntry.missionType().id());
            tag.putInt("required_amount", item.getValue().getRandomValue());
            tag.putString("title", getRandomEntry(randomEntry.titles()));

            list.add(tag);
        }
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
                    type.getProgress(player, item()),
                    type.getPreviewStack(item()),
                    type.getItemStack(item()),
                    type.description(),
                    isCompleted()
            );
        }
    }
}
