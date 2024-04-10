package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface MultiResourceMissionType extends MissionType {
    @Override
    default int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt(item.toString());
    }

    @Override
    default void reset(UUID player, ResourceLocation item) {
        getData(player).remove(item.toString());
    }

    @Override
    default void increment(int amount, ResourceLocation item, CompoundTag data) {
        String itemString = item.toString();
        data.putInt(itemString, data.getInt(itemString) + amount);
    }

    @Override
    default boolean isLonely() {
        return false;
    }
}
