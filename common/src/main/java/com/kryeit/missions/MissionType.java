package com.kryeit.missions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface MissionType {
    String id();

    String difficulty();

    Component taskString(String language, int progress, Component itemName);

    Component titleString();

    int getProgress(UUID player, ResourceLocation item);

    void reset(UUID player);

    default CompoundTag getData(UUID player) {
        return DataStorage.INSTANCE.getMissionData(id(), player);
    }
}
