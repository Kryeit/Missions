package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatisticMission {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation("missions", "statistic");
    private static final Map<ResourceLocation, MissionType> missions = new HashMap<>();

    public static void handleStatisticChange(UUID player, int difference, ResourceLocation statistic) {
        MissionType type = missions.get(statistic);
        if (type == null || difference == 0) return;

        CompoundTag data = type.getData(player);
        data.putInt("value", data.getInt("value") + difference);
        MissionManager.checkReward(type, player, IDENTIFIER);
    }

    public static MissionType createStatisticMission(String id, MissionDifficulty difficulty, Component description, ResourceLocation... statistics) {
        MissionType type = createType(id, difficulty, description);
        for (ResourceLocation statistic : statistics) {
            missions.put(statistic, type);
        }
        return type;
    }

    private static MissionType createType(String id, MissionDifficulty difficulty, Component description) {
        return new MissionType() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public MissionDifficulty difficulty() {
                return difficulty;
            }

            @Override
            public Component description() {
                return description;
            }

            @Override
            public int getProgress(UUID player, ResourceLocation item) {
                return getData(player).getInt("value");
            }

            @Override
            public void reset(UUID player) {
                getData(player).remove("value");
            }
        };
    }
}
