package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

public class BreakMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "break";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Break mission");
    }
}
