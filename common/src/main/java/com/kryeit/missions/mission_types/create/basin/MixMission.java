package com.kryeit.missions.mission_types.create.basin;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class MixMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "mix";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.EASY;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Mixing mission");
    }
}
