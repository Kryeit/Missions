package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

public class KillMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "kill";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Kill mission");
    }
}
