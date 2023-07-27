package com.kryeit;


import com.kryeit.missions.DataStorage;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.mission_types.*;
import com.kryeit.missions.mission_types.create.CrushingMission;
import com.kryeit.missions.mission_types.create.CuttingMission;
import com.kryeit.missions.mission_types.create.PressMission;
import com.kryeit.missions.mission_types.create.basin.CompactingMission;
import com.kryeit.missions.mission_types.create.basin.MixingMission;
import com.kryeit.missions.mission_types.create.fan.BlastingMission;
import com.kryeit.missions.mission_types.create.fan.HauntingMission;
import com.kryeit.missions.mission_types.create.fan.SmokingMission;
import com.kryeit.missions.mission_types.create.fan.SplashingMission;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static final String MOD_ID = "missions";
    private static ConfigReader configReader;

    public static void init() {
        registerMissions();

        try {
            configReader = ConfigReader.readFile(Path.of("missions/missions.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(DataStorage.INSTANCE::save));
    }

    public static ConfigReader getConfig() {
        return configReader;
    }

    public static void registerMissions() {
        MissionTypeRegistry.INSTANCE.register(new BreakMission());
        MissionTypeRegistry.INSTANCE.register(new CraftMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());
        MissionTypeRegistry.INSTANCE.register(new EatMission());
        MissionTypeRegistry.INSTANCE.register(new VoteMission());
        MissionTypeRegistry.INSTANCE.register(new PressMission());
        MissionTypeRegistry.INSTANCE.register(new CrushingMission());
        MissionTypeRegistry.INSTANCE.register(new CuttingMission());

        // Basin
        MissionTypeRegistry.INSTANCE.register(new MixingMission());
        MissionTypeRegistry.INSTANCE.register(new CompactingMission());

        // Fan
        MissionTypeRegistry.INSTANCE.register(new HauntingMission());
        MissionTypeRegistry.INSTANCE.register(new SplashingMission());
        MissionTypeRegistry.INSTANCE.register(new BlastingMission());
        MissionTypeRegistry.INSTANCE.register(new SmokingMission());
    }
}
