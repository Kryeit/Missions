package com.kryeit.missions;

import com.kryeit.missions.utils.Range;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Map<MissionType, Mission> missions;

    public Config(Map<MissionType, Mission> missions) {
        this.missions = missions;
    }

    public static Config readFile(Path file) throws IOException {
        Map<MissionType, Mission> missions = new HashMap<>();
        String string = Files.readString(file);
        JSONObject object = new JSONObject(string);

        for (String key : object.keySet()) {
            JSONObject value = object.getJSONObject(key);
            JSONObject reward = value.getJSONObject("reward");

            MissionType missionType = MissionTypeRegistry.INSTANCE.getType(key);
            Mission mission = new Mission(
                    Range.fromString(reward.getString("amount")),
                    reward.getString("item"),
                    missionType,
                    getItems(value.getJSONObject("com/kryeit/missions"))
            );
            missions.put(missionType, mission);
        }
        return new Config(missions);
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
