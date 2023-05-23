package com.kryeit.missions;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MissionManager {
    public static void checkReward(MissionType type, UUID player) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), player);
        if (activeMission == null || activeMission.isCompleted()) return;

        if (type.getProgress(player, activeMission.item()) >= activeMission.requiredAmount()) {
            Config.Mission mission = gibConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String item = mission.rewardItem();

            type.reset(player);
            DataStorage.INSTANCE.addReward(player, item, rewardAmount);
        }
    }

    public static List<DataStorage.ActiveMission> getActiveMissions(UUID playerId) {
        return DataStorage.INSTANCE.getActiveMissions(playerId);
    }

    public static DataStorage.ActiveMission getActiveMission(String id, UUID player) {
        List<DataStorage.ActiveMission> missions = DataStorage.INSTANCE.getActiveMissions(player);
        for (DataStorage.ActiveMission mission : missions) {
            if (mission.missionID().equals(id)) {
                return mission;
            }
        }
        return null;
    }

    public static Config gibConfig() {
        try {
            return Config.readFile(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
