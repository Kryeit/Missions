package com.kryeit.missions.mission_types.vanilla;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class DrinkMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "drink";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Drinking mission");
    }
}
