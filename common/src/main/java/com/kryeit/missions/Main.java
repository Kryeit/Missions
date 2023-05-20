package com.kryeit.missions;

import com.kryeit.missions.missions.DriveTrainMission;
import com.kryeit.missions.missions.VoteMission;

public class Main {
    public static void main(String[] args) {
        MissionTypeRegistry registry = new MissionTypeRegistry();
        registry.register(new VoteMission());
        registry.register(new DriveTrainMission());
    }
}
