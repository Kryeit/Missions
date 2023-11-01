package com.kryeit.missions.mission_types.create.vanilla;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
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
        return Component.nullToEmpty("Breaking mission");
    }
}
