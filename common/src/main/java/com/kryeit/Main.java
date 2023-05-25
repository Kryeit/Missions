package com.kryeit;

import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.BreakMission;
import com.kryeit.missions.mission_types.KillMission;

public class Main {
    public static final String MOD_ID = "missions";

    public static void init() {
        MissionTypeRegistry.INSTANCE.register(new BreakMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
