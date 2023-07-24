package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

public class EatMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "eat";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Eat mission");
    }
}
