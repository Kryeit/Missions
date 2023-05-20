package com.kryeit.missions;

import com.kryeit.missions.wrappers.Player;
import com.kryeit.missions.wrappers.PlayerData;

import java.util.List;

public class DataStorage {
    public static final DataStorage INSTANCE = new DataStorage();

    public PlayerData getData(String missionID, Player player) {
        return null;
    }

    public List<ActiveMission> getActiveMissions(Player player) {
        return List.of();
    }
}
