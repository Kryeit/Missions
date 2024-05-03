package com.kryeit.missions.mission_types.vanilla;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class FishMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "fish";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Fishing mission");
    }

    @Override
    public boolean assignOnlyOnce() {
        return true;
    }
}
