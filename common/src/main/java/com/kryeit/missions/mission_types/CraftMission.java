package com.kryeit.missions.mission_types;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CraftMission implements ItemMissionType {

    private static final Random RAND = new Random();
    private static final List<String> TITLES = Arrays.asList(
            "Masterful Manufacture Madness",
            "Bizarre Blueprint Builder",
            "Crafty Creation Crusade",
            "Widget Weaving Wizard",
            "Artisanal Assembly Adventure"
    );
    @Override
    public String id() {
        return "craft";
    }

    @Override
    public String difficulty() {
        return "normal";
    }

    public Component titleString() {
        String title = TITLES.get(RAND.nextInt(TITLES.size()));
        return Component.nullToEmpty(title);
    }

    @Override
    public Component taskString(String language, int progress, Component itemName) {
        return Component.nullToEmpty("Craft mission");
    }
}
