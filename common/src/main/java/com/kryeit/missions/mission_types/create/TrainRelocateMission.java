package com.kryeit.missions.mission_types.create;

import com.kryeit.Missions;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.simibubi.create.AllItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class TrainRelocateMission implements MissionType {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation(Missions.MOD_ID, "distance");

    public static void handleDistanceChange(UUID player, int difference) {
        MissionManager.incrementMission(player, TrainRelocateMission.class, IDENTIFIER, difference);
    }

    @Override
    public String id() {
        return "relocate";
    }

    @Override
    public boolean assignOnlyOnce() {
        return false;
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Train relocation mission");
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
        return AllItems.WRENCH.asStack();
    }

}
