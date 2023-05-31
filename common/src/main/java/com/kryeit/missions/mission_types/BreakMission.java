package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class BreakMission implements MissionType {
    @Override
    public String id() {
        return "break";
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        int anInt = getData(player).getInt(item.toString());
        System.out.println(anInt);
        return anInt;
    }

    @Override
    public void reset(UUID player) {
        CompoundTag data = getData(player);
        for (String key : data.getAllKeys()) {
            data.remove(key);
        }
    }

    public void handleBreak(UUID player, ResourceLocation item) {
        if (MissionManager.countItem(id(), player, item)) {
            CompoundTag data = getData(player);
            String itemString = item.toString();
            data.putInt(itemString, data.getInt(itemString) + 1);

            MissionManager.checkReward(this, player, item);
        }
    }
}
