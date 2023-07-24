package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionManager;
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
    default void reset(UUID player) {
        getData(player).getAllKeys().clear();
    }

    default void handleItem(UUID player, ResourceLocation item) {
        handleItem(player, item, 1);
    }

    default void handleItem(UUID player, ResourceLocation item, int amount) {
        if (MissionManager.countItem(id(), player, item)) {
            CompoundTag data = getData(player);
            String itemString = item.toString();
            data.putInt(itemString, data.getInt(itemString) + amount);

            MissionManager.checkReward(this, player, item);
        }
    }
}
