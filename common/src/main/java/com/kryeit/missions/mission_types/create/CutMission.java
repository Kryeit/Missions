package com.kryeit.missions.mission_types.create;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class CutMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "cut";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Cutting mission");
    }
}
