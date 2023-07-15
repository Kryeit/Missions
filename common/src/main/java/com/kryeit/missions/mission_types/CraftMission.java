package com.kryeit.missions.mission_types;

import net.minecraft.network.chat.Component;

public class CraftMission implements ItemMissionType {
    @Override
    public String id() {
        return "craft";
    }

    @Override
    public Component taskString(String language, int progress, Component itemName) {
        return Component.nullToEmpty("Break mission");
    }
}
