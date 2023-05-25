package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class KillMission implements MissionType {
    @Override
    public String id() {
        return "kill";
    }

    @Override
    public int getProgress(UUID player, String item) {
        return getData(player).getInt(item);
    }

    @Override
    public void reset(UUID player) {
        CompoundTag data = getData(player);
        for (String key : data.getAllKeys()) {
            data.remove(key);
        }
    }

    public void handleKill(UUID player, String item) {
        CompoundTag data = getData(player);
        data.putInt(item, data.getInt(item) + 1);
    }
}
