package com.kryeit.missions;

import com.kryeit.JSONObject;
import com.kryeit.missions.utils.Range;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
    private final Map<MissionType, Mission> missions;

    private ConfigReader(Map<MissionType, Mission> missions) {
        this.missions = missions;
    }

    public static ConfigReader readFile(Path path) throws IOException {
        if (!path.toFile().exists()) {
            InputStream stream = ConfigReader.class.getResourceAsStream("/example_config.json");
            if (stream == null) throw new NullPointerException("Cannot find example config");
            Files.copy(stream, path);
        }

        Map<MissionType, Mission> missions = new HashMap<>();
        String string = Files.readString(path);
        JSONObject object = new JSONObject(string);

        for (String key : object.keySet()) {
            JSONObject value = object.getObject(key);
            JSONObject reward = value.getObject("reward");

            MissionType missionType = MissionTypeRegistry.INSTANCE.getType(key);
            Mission mission = new Mission(
                    Range.fromString(reward.getString("amount")),
                    reward.getString("item"),
                    missionType,
                    getItems(value.getObject("missions"))
            );
            missions.put(missionType, mission);
        }
        return new ConfigReader(missions);
    }

    private static Map<String, Range> getItems(JSONObject items) {
        Map<String, Range> itemsMap = new HashMap<>();
        for (String itemKey : items.keySet()) {
            itemsMap.put(itemKey, Range.fromString(items.getString(itemKey)));
        }
        return itemsMap;
    }

    public Map<MissionType, Mission> getMissions() {
        return missions;
    }

    public static class Mission {
        private final Range rewardAmount;
        private final String rewardItem;
        private final MissionType missionType;
        private final Map<String, Range> items;

        public Mission(Range rewardAmount, String rewardItem, MissionType missionType, Map<String, Range> items) {
            this.rewardAmount = rewardAmount;
            this.rewardItem = rewardItem;
            this.missionType = missionType;
            this.items = items;
        }

        public Map<String, Range> items() {
            return items;
        }

        public MissionType missionType() {
            return missionType;
        }

        public String rewardItem() {
            return rewardItem;
        }

        public Range rewardAmount() {
            return rewardAmount;
        }
    }
}
