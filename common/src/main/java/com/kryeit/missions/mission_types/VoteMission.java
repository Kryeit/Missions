package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class VoteMission implements MissionType {
    private static final ResourceLocation IDENTIFIER_LOCATION = new ResourceLocation("vote_mission", "vote");
    @Override
    public String id() {
        return "vote";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt("votes");
    }

    @Override
    public void reset(UUID player) {
        getData(player).remove("votes");
    }

    @Override
    public void increment(int amount, ResourceLocation item, CompoundTag data) {
        data.putInt("votes", data.getInt("votes") + 1);
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Voting mission");
    }

    @Override
    public ItemStack getItemStack(ResourceLocation item) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public ItemStack getPreviewStack(ResourceLocation item) {
        return Items.TOTEM_OF_UNDYING.getDefaultInstance();
    }
}
