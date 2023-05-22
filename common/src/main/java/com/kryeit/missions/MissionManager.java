package com.kryeit.missions;

import com.kryeit.missions.wrappers.Player;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MissionManager {
    public static void checkReward(MissionType type, Player player) {
        ActiveMission activeMission = getActiveMission(type.id(), player);
        if (activeMission == null || activeMission.isCompleted()) return;

        if (type.getProgress(player, activeMission.item()) >= activeMission.requiredAmount()) {
            Config.Mission mission = gibConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String item = mission.rewardItem();

            type.reset(player);
            activeMission.setCompleted(true);
        }
    }

    public static List<ActiveMission> getActiveMissions(UUID playerId) {
        return List.of();
    }

    public static ActiveMission getActiveMission(String id, Player player) {
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
