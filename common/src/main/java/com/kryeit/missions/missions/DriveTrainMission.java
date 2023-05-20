package com.kryeit.missions.missions;

import com.kryeit.missions.MissionType;
import com.kryeit.missions.wrappers.Player;

public class DriveTrainMission implements MissionType {
    @Override
    public String id() {
        return "drive_train";
    }

    @Override
    public int getProgress(Player player, String item) {
        return 0;
    }

    @Override
    public void reset(Player player) {

    }
}
