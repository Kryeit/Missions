package com.kryeit.missions.mission_types.create.diving;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.simibubi.create.AllItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class DivingMissionType implements MissionType {

    public static void handleTimeChange(UUID player, int difference, ResourceLocation fluid) {
        MissionManager.incrementMission(player, DivingMissionType.class, fluid, difference);
    }

    @Override
    public String id() {
        return "dive";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Diving mission");
    }

    @Override
    public ItemStack getItemStack(ResourceLocation fluid) {
        return BuiltInRegistries.FLUID.get(fluid).getBucket().getDefaultInstance();
    }

    @Override
    public ItemStack getPreviewStack(ResourceLocation fluid) {
        return fluid.toString().equals("minecraft:lava") ? AllItems.NETHERITE_DIVING_HELMET.asStack()
                : AllItems.COPPER_DIVING_HELMET.asStack();
    }

    @Override
    public int getProgress(UUID player, ResourceLocation fluid) {
        return getData(player).getInt(fluid.toString()) / (30 * 60);
    }

    @Override
    public void reset(UUID player) {
        getData(player).getAllKeys().clear();
    }

    @Override
    public void increment(int amount, ResourceLocation fluid, CompoundTag data) {
        String fluidString = fluid.toString();
        data.putInt(fluidString, data.getInt(fluidString) + amount);
    }

}
