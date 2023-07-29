package com.kryeit.missions.mission_types;

import com.kryeit.Utils;
import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FishMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "fish";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Fishing mission");
    }

    @Override
    public ItemStack getItemStack(ResourceLocation item) {
        return Utils.getSpawnEggOfEntity(item);
    }
}
