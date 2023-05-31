package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionType;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class EatMission implements MissionType {
    @Override
    public String id() {
        return "eat";
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return 0;
    }

    @Override
    public void reset(UUID player) {

    }
}
