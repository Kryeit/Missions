package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

public class PressMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "press";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Press Mission");
    }
}
