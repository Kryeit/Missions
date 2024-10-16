package com.kryeit.missions.config;

import com.kryeit.JSONObject;
import com.kryeit.JSONObject.JSONArray;
import com.kryeit.compat.CompatAddon;
import com.kryeit.missions.MissionType;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    private final Map<MissionType, MissionTypeConfig> missions;
    private final List<ItemStack> exchange;
    public final float exchangerDropRate;
    public final int firstRerollCurrency;
    public final int freeRerolls;
    public final String commandUponMission;
    public final int numberOfMissions;
    public final ReassignInterval reassignInterval;

    private ConfigReader(Map<MissionType, MissionTypeConfig> missions, List<ItemStack> exchange, float exchangerDropRate, int firstRerollCurrency, int freeRerolls, String commandUponMission, int numberOfMissions, ReassignInterval reassignInterval) {
        this.missions = missions;
        this.exchange = exchange;
        this.exchangerDropRate = exchangerDropRate;
        this.firstRerollCurrency = firstRerollCurrency;
        this.freeRerolls = freeRerolls;
        this.commandUponMission = commandUponMission;
        this.numberOfMissions = numberOfMissions;
        this.reassignInterval = reassignInterval;
    }

    public static ConfigReader readFile(Path path) throws IOException {
        String content = readOrCopyFile(path.resolve("missions.json"), "/missions.json");

        JSONObject object = new JSONObject(content);
        int configVersion = object.optInt("config-version").orElse(1);

        Map<MissionType, MissionTypeConfig> missionConfig = new HashMap<>();
        switch (configVersion) {
            case 1 -> parseConfigVersion1(object, missionConfig);
            case 2 -> parseConfigVersion2(object, missionConfig);
        }

        String exchange;
        if (CompatAddon.NUMISMATICS.isLoaded()) {
            exchange = readOrCopyFile(path.resolve("currency.json"), "/numismatics/currency.json");
        } else {
            exchange = readOrCopyFile(path.resolve("currency.json"), "/currency.json");
        }

        JSONArray jsonArray = new JSONArray(exchange);
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject itemObj = jsonArray.getObject(i);
            itemObj.keySet().forEach(key -> {
                int quantity = Integer.parseInt(itemObj.getString(key));
                ResourceLocation location = new ResourceLocation(key);
                ItemStack itemStack = Utils.getItem(location, quantity);
                items.add(itemStack);
            });
        }

        String config = readOrCopyFile(path.resolve("config.json"), "/config.json");
        JSONObject configObject = new JSONObject(config);

        float exchangerDropRate = Float.parseFloat(configObject.getString("exchanger-drop-rate"));
        int firstRerollCurrency = Integer.parseInt(configObject.getString("first-reroll-currency"));
        int freeRerolls = Integer.parseInt(configObject.getString("free-rerolls"));
        String commandUponMission = configObject.getString("command-upon-mission");
        int numberOfMissions = configObject.optInt("number-of-missions").orElse(10); // TODO implement
        String reassignmentInterval = configObject.optString("reassignment-interval (DAILY or WEEKLY)").orElse("WEEKLY");

        return new ConfigReader(missionConfig, items, exchangerDropRate, firstRerollCurrency, freeRerolls, commandUponMission, numberOfMissions, ReassignInterval.valueOf(reassignmentInterval));
    }

    private static void parseConfigVersion2(JSONObject config, Map<MissionType, MissionTypeConfig> missions) {
        for (String key : config.keySet()) {
            if (key.equals("config-version")) continue;

            JSONObject value = config.getObject(key);
            float weight = value.optFloat("weight").orElse(1f);
            if (weight == 0) continue;

            MissionType missionType = MissionTypeRegistry.INSTANCE.getType(key);

            Map<Map<String, Range>, SubMissionConfig> subMissions = new HashMap<>();

            List<SubMissionConfig> subMissionConfigList = value.getArray("missions").asList((array, i) -> {
                JSONObject entry = array.getObject(i);
                return new SubMissionConfig(
                        entry.getArray("titles").asList(JSONArray::getString),
                        getItems(entry.getObject("items")),
                        getRewards(entry.getArray("rewards"))
                );
            });
            for (SubMissionConfig subMissionConfig : subMissionConfigList) {
                subMissions.put(subMissionConfig.items(), subMissionConfig);
            }

            MissionTypeConfig missionTypeConfig = new MissionTypeConfig(
                    missionType,
                    weight,
                    subMissions
            );
            missions.put(missionType, missionTypeConfig);
        }
    }

    private static void parseConfigVersion1(JSONObject config, Map<MissionType, MissionTypeConfig> missions) {
        for (String key : config.keySet()) {
            JSONObject value = config.getObject(key);
            JSONObject reward = value.getObject("reward");
            float weight = value.optFloat("weight").orElse(1f);
            if (weight == 0) continue;

            MissionType missionType = MissionTypeRegistry.INSTANCE.getType(key);

            Map<String, Range> items = getItems(value.getObject("missions"));

            MissionTypeConfig mtc = new MissionTypeConfig(
                    missionType,
                    weight,
                    Map.of(
                            items, new SubMissionConfig(
                                    value.getArray("titles").asList(JSONArray::getString),
                                    items,
                                    List.of(new Reward(reward.getString("item"), Range.fromString(reward.getString("amount"))))
                            )
                    )
            );

            missions.put(missionType, mtc);
        }
    }

    private static List<Reward> getRewards(JSONArray rewards) {
        return rewards.asList((array, i) -> {
            JSONObject entry = rewards.getObject(i);
            return new Reward(
                    entry.getString("item"),
                    Range.fromString(entry.getString("amount"))
            );
        });
    }

    public static String readOrCopyFile(Path path, String exampleFile) throws IOException {
        File file = path.toFile();
        if (!file.exists()) {
            InputStream stream = ConfigReader.class.getResourceAsStream(exampleFile);
            if (stream == null) throw new NullPointerException("Cannot load example file");

            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            Files.copy(stream, path);
        }
        return Files.readString(path);
    }

    private static Map<String, Range> getItems(JSONObject items) {
        Map<String, Range> itemsMap = new HashMap<>();
        for (String itemKey : items.keySet()) {

            itemsMap.put(itemKey, Range.fromString(items.getString(itemKey)));
        }
        return itemsMap;
    }

    public Map<MissionType, MissionTypeConfig> getMissions() {
        return missions;
    }

    public List<ItemStack> exchange() {
        return exchange;
    }

    public record MissionTypeConfig(
            MissionType missionType,
            float weight,
            Map<Map<String, Range>, SubMissionConfig> subMissions
    ) {
    }

    public enum ReassignInterval {
        DAILY,
        WEEKLY
    }

    public record SubMissionConfig(List<String> titles, Map<String, Range> items, List<Reward> rewards) {
    }

    public record Reward(String item, Range count) {
    }
}
