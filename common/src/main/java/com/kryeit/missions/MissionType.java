package com.kryeit.missions;

import com.kryeit.missions.wrappers.Player;
import com.kryeit.missions.wrappers.PlayerData;

public interface MissionType {
    String id();

    int getProgress(Player player, String item);

    void reset(Player player);

    default PlayerData getData(Player player) {
        return DataStorage.INSTANCE.getData(id(), player);
    }
}
