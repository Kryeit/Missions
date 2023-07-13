package com.kryeit;



import com.kryeit.missions.ConfigReader;
import com.kryeit.missions.DataStorage;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static final String MOD_ID = "missions";
    private static ConfigReader configReader;

    public static final Logger LOGGER = LogManager.getLogger();


    public static void init() {
        MissionTypeRegistry.INSTANCE.register(new BreakMission());
        MissionTypeRegistry.INSTANCE.register(new CraftMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());
        MissionTypeRegistry.INSTANCE.register(new EatMission());
        MissionTypeRegistry.INSTANCE.register(new VoteMission());

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
        if (!PlatformSpecific.isClient()) {
            try {
                configReader = ConfigReader.readFile(Path.of("missions/config.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(DataStorage.INSTANCE::save));
    }

    public static ConfigReader getConfig() {
        return configReader;
    }
}
