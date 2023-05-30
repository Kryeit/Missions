package com.kryeit.missions;

import com.kryeit.Utils;
import com.kryeit.missions.utils.Range;
import net.minecraft.nbt.*;

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
        for(T t: collection) if (--num < 0) return t;
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
        CompoundTag missionData = data.getCompound("mission_data");
        if (!missionData.contains("mission_data")) data.put("mission_data", missionData);

        CompoundTag missionIDData = missionData.getCompound(missionID);
        if (!missionData.contains(missionID)) missionData.put(missionID, missionIDData);

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
        CompoundTag activeMissions = data.getCompound("active_missions");
        if (!data.contains("active_missions")) data.put("active_missions", activeMissions);

        ListTag list = activeMissions.getList(uuidString, Tag.TAG_COMPOUND);
        if (!activeMissions.contains(uuidString)) activeMissions.put(uuidString, list);
        return list;
    }

    public List<ActiveMission> getActiveMissions(UUID player) {
        ListTag list = getActiveMissionsTag(player);

        List<ActiveMission> output = new ArrayList<>();
        for (Tag tag : list) {
            CompoundTag compound = (CompoundTag) tag;
            String item = compound.getString("item");
            boolean isCompleted = compound.getBoolean("completed");
            String missionID = compound.getString("mission_id");
            int requiredAmount = compound.getInt("required_amount");
            output.add(new ActiveMission(item, isCompleted, missionID, requiredAmount));
        }
        return output;
    }

    public void completeMission(UUID player, String item, String missionTypeID, String rewardItem, int rewardAmount) {
        ListTag list = getActiveMissionsTag(player);
        for (Tag tag : list) {
            CompoundTag compound = (CompoundTag) tag;
            if (compound.getString("item").equals(item) && compound.getString("mission_id").equals(missionTypeID)) {
                if (!compound.getBoolean("completed")) {
                    addReward(player, rewardItem, rewardAmount);
                }

                compound.putBoolean("completed", true);
                break;
            }
        }
    }

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

            list.add(tag);
        }
    }

    public int getLastAssignedDay(UUID player) {
        CompoundTag lastAssigned = data.getCompound("last_assigned");
        if (!data.contains("last_assigned")) data.put("last_assigned", lastAssigned);
        return lastAssigned.getInt(player.toString());
    }

    public void setLastAssignedDay(UUID player) {
        setLastAssignedDay(player, Utils.getDay());
    }

    public void setLastAssignedDay(UUID player, int day) {
        CompoundTag lastAssigned = data.getCompound("last_assigned");
        if (!data.contains("last_assigned")) data.put("last_assigned", lastAssigned);
        lastAssigned.putInt(player.toString(), day);
    }

    public void addReward(UUID player, String item, int amount) {
        CompoundTag rewards = data.getCompound("rewards");
        if (!data.contains("rewards")) data.put("rewards", rewards);

        CompoundTag playerRewards = rewards.getCompound(player.toString());
        if (!data.contains("rewards")) data.put("rewards", rewards);
        playerRewards.putInt(item, playerRewards.getInt(item) + amount);
    }

    public Map<String, Integer> getUnclaimedRewards(UUID player) {
        CompoundTag rewards = data.getCompound("rewards");
        if (!data.contains("rewards")) data.put("rewards", rewards);

        Map<String, Integer> output = new HashMap<>();
        CompoundTag playerRewards = rewards.getCompound(player.toString());
        for (String key : playerRewards.getAllKeys()) {
            output.put(key, rewards.getInt(key));
        }
        return output;
    }

    /**
     * Claims the player's rewards. Does NOT move the rewards into the player's inventory!
     * @param player The player whose rewards to claim
     */
    public void claimRewards(UUID player) {
        CompoundTag rewards = data.getCompound("rewards");
        rewards.remove(player.toString());
    }

    public static class ActiveMission {
        private final String item;
        private final boolean isCompleted;
        private final String missionID;
        private final int requiredAmount;

        private ActiveMission(String item, boolean isCompleted, String missionID, int requiredAmount) {
            this.item = item;
            this.isCompleted = isCompleted;
            this.missionID = missionID;
            this.requiredAmount = requiredAmount;
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

        public String item() {
            return item;
        }
    }
}
