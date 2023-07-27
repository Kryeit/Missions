package com.kryeit.missions.mission_types.create;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class CrushingMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "crush";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Crushing mission");
    }
}
