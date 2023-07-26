package com.kryeit;


import com.kryeit.missions.DataStorage;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.mission_types.*;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static final String MOD_ID = "missions";
    private static ConfigReader configReader;

    public static void init() {
        MissionTypeRegistry.INSTANCE.register(new BreakMission());
        MissionTypeRegistry.INSTANCE.register(new CraftMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());
        MissionTypeRegistry.INSTANCE.register(new EatMission());
        MissionTypeRegistry.INSTANCE.register(new VoteMission());
        MissionTypeRegistry.INSTANCE.register(new PressMission());
        MissionTypeRegistry.INSTANCE.register(new MixingMission());
        MissionTypeRegistry.INSTANCE.register(new CompactingMission());
        MissionTypeRegistry.INSTANCE.register(new CrushingMission());



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
}
