package com.kryeit.missions.mission_types.create.contraption;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.simibubi.create.AllBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SawMission implements MultiResourceMissionType {

    @Override
    public String id() {
        return "saw";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Sawing mission");
    }

    @Override
    public ItemStack getPreviewStack(ResourceLocation item) {
        return AllBlocks.MECHANICAL_SAW.asStack();
    }
}
