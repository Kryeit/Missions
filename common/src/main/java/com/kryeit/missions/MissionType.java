package com.kryeit.missions;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public interface MissionType {
    String id();

    int getProgress(UUID player, String item);

    void reset(UUID player);

    default CompoundTag getData(UUID player) {
        return DataStorage.INSTANCE.getMissionData(id(), player);
    }
}
