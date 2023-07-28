package com.kryeit;


import com.kryeit.missions.DataStorage;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.mission_types.*;
import com.kryeit.missions.mission_types.create.CrushMission;
import com.kryeit.missions.mission_types.create.CutMission;
import com.kryeit.missions.mission_types.create.MillMission;
import com.kryeit.missions.mission_types.create.PressMission;
import com.kryeit.missions.mission_types.create.basin.CompactMission;
import com.kryeit.missions.mission_types.create.basin.MixMission;
import com.kryeit.missions.mission_types.create.fan.BlastMission;
import com.kryeit.missions.mission_types.create.fan.HauntMission;
import com.kryeit.missions.mission_types.create.fan.SmokeMission;
import com.kryeit.missions.mission_types.create.fan.SplashMission;

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
        MissionTypeRegistry.INSTANCE.register(new PlaceMission());
        MissionTypeRegistry.INSTANCE.register(new FishMission());
        MissionTypeRegistry.INSTANCE.register(new CraftMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());
        MissionTypeRegistry.INSTANCE.register(new EatMission());
        MissionTypeRegistry.INSTANCE.register(new VoteMission());
        MissionTypeRegistry.INSTANCE.register(new PressMission());
        MissionTypeRegistry.INSTANCE.register(new CrushMission());
        MissionTypeRegistry.INSTANCE.register(new CutMission());
        MissionTypeRegistry.INSTANCE.register(new MillMission());

        // Basin
        MissionTypeRegistry.INSTANCE.register(new MixMission());
        MissionTypeRegistry.INSTANCE.register(new CompactMission());

        // Fan
        MissionTypeRegistry.INSTANCE.register(new HauntMission());
        MissionTypeRegistry.INSTANCE.register(new SplashMission());
        MissionTypeRegistry.INSTANCE.register(new BlastMission());
        MissionTypeRegistry.INSTANCE.register(new SmokeMission());
    }
}
