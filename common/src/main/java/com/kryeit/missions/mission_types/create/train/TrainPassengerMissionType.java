package com.kryeit.missions.mission_types.create.train;

import com.kryeit.Missions;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.simibubi.create.AllBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;
import java.util.UUID;

public class TrainPassengerMissionType implements MissionType {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation(Missions.MOD_ID, "distance");

    public static void handleDistanceChange(UUID player, int difference) {
        MissionManager.incrementMission(player, TrainPassengerMissionType.class, IDENTIFIER, difference);
    }

    @Override
    public String id() {
        return "train-passenger";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Train passenger mission");
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt("value") / 1_000;
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
        return AllBlocks.SEATS.get(DyeColor.byId(new Random().nextInt(0, 15))).asStack();
    }

}
