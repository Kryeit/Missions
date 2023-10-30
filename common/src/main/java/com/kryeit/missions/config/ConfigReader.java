package com.kryeit.missions.config;

import com.kryeit.JSONObject;
import com.kryeit.JSONObject.JSONArray;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kryeit.coins.Coins.EXCHANGE_RATE;

public class ConfigReader {
    private final Map<MissionType, Mission> missions;
    private final List<ItemStack> exchange;

    private ConfigReader(Map<MissionType, Mission> missions, List<ItemStack> exchange) {
        this.missions = missions;
        this.exchange = exchange;
    }

    public static ConfigReader readFile(Path path) throws IOException {
        String content = readOrCopyFile(path.resolve("missions.json"), "/example_config.json");

        Map<MissionType, Mission> missions = new HashMap<>();
        JSONObject object = new JSONObject(content);

        for (String key : object.keySet()) {
            JSONObject value = object.getObject(key);
            JSONObject reward = value.getObject("reward");

            MissionType missionType = MissionTypeRegistry.INSTANCE.getType(key);
            Mission mission = new Mission(
                    Range.fromString(reward.getString("amount")),
                    reward.getString("item"),
                    missionType,
                    getItems(value.getObject("missions")),
                    value.getArray("titles").asList(JSONArray::getString)
            );
            missions.put(missionType, mission);
        }

        String exchange = readOrCopyFile(path.resolve("currency.json"), "/example_currency.json");
        List<ItemStack> items = new JSONArray(exchange).asList((array, integer) -> {
            ResourceLocation location = new ResourceLocation(array.getString(integer));
            return Utils.getItem(location);
        });

        String config = readOrCopyFile(path.resolve("config.json"), "/example_modconfig.json");
        JSONObject configObject = new JSONObject(config);
        EXCHANGE_RATE = Integer.parseInt(configObject.getString("exchange-rate"));

        return new ConfigReader(missions, items);
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

    public Map<MissionType, Mission> getMissions() {
        return missions;
    }

    public List<ItemStack> exchange() {
        return exchange;
    }

    public record Mission(Range rewardAmount, String rewardItem, MissionType missionType, Map<String, Range> items,
                          List<String> titles) {
    }
}
