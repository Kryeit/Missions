package com.kryeit.missions.mission_types.create.vanilla;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.utils.Utils;
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
