package com.kryeit;



import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static final String MOD_ID = "missions";
 //   private static ConfigReader configReader;

    public static void init() {
//        MissionTypeRegistry.INSTANCE.register(new BreakMission());
//        MissionTypeRegistry.INSTANCE.register(new CraftMission());
//        MissionTypeRegistry.INSTANCE.register(new KillMission());
//        MissionTypeRegistry.INSTANCE.register(new EatMission());
//        MissionTypeRegistry.INSTANCE.register(new VoteMission());

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
//        try {
//            configReader = ConfigReader.readFile(Path.of("missions/config.json"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Runtime.getRuntime().addShutdownHook(new Thread(DataStorage.INSTANCE::save));
//    }
//
//    public static ConfigReader getConfig() {
//        return configReader;
    }
}
