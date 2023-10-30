package com.kryeit.missions.mission_types;

import com.kryeit.Main;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatisticMission {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation(Main.MOD_ID, "statistic");
    private static final Map<ResourceLocation, StatisticMissionType> missions = new HashMap<>();

    public static void handleStatisticChange(UUID player, int difference, ResourceLocation statistic) {
        StatisticMissionType type = missions.get(statistic);
        if (type == null || difference == 0) return;

        MissionManager.incrementMission(player, type, IDENTIFIER, difference);
    }

    public static MissionType createStatisticMission(String id, MissionDifficulty difficulty, Component description, float divisor, Item item, ResourceLocation... statistics) {
        StatisticMissionType type = new StatisticMissionType(id, difficulty, description, divisor, item);
        for (ResourceLocation statistic : statistics) {
            missions.put(statistic, type);
        }
        return type;
    }

    private record StatisticMissionType(String id, MissionDifficulty difficulty, Component description,
                                        float divisor, Item item) implements MissionType {

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
            return (int) (getData(player).getInt("value") / divisor);
        }

        @Override
        public void reset(UUID player) {
            getData(player).remove("value");
        }

        @Override
        public void increment(int amount, ResourceLocation item, CompoundTag data) {
            data.putInt("value", data.getInt("value") + amount);
        }

        @Override
        public ItemStack getItemStack(ResourceLocation item) {
            return Items.AIR.getDefaultInstance();
        }

        @Override
        public ItemStack getPreviewStack(ResourceLocation item) {
            return item().getDefaultInstance();
        }
    }
}
