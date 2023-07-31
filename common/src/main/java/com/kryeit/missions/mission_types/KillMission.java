package com.kryeit.missions.mission_types;

import com.kryeit.utils.Utils;
import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class KillMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "kill";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Killing mission");
    }

    @Override
    public ItemStack getItemStack(ResourceLocation item) {
        return Utils.getSpawnEggOfEntity(item);
    }
}
