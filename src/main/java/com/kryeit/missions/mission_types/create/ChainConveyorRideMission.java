package com.kryeit.missions.mission_types.create;

import com.kryeit.Missions;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.simibubi.create.AllBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

/**
 * Distance mission: ride a Create 6 chain conveyor while holding a wrench, measured in blocks travelled.
 * Mirrors {@link com.kryeit.missions.mission_types.create.belt.BeltWalkMission}; the riding state is read
 * from Create's server-side hanging-players map in ChainConveyorRideMixin.
 */
public class ChainConveyorRideMission implements MissionType {
    private static final ResourceLocation IDENTIFIER = Missions.asResource("distance");

    public static void handleDistanceChange(UUID player, int difference) {
        MissionManager.incrementMission(player, ChainConveyorRideMission.class, IDENTIFIER, difference);
    }

    @Override
    public String id() {
        return "chain-ride";
    }

    @Override
    public boolean assignOnlyOnce() {
        return true;
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Chain conveyor riding mission");
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt("value");
    }

    @Override
    public void reset(UUID player, ResourceLocation item) {
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
        return AllBlocks.CHAIN_CONVEYOR.asStack();
    }
}
