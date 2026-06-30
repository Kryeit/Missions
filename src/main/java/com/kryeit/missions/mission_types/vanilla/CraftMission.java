package com.kryeit.missions.mission_types.vanilla;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import net.minecraft.network.chat.Component;

public class CraftMission implements MultiResourceMissionType {
    @Override
    public String id() {
        return "craft";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Crafting mission");
    }
}
