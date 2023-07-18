package com.kryeit.missions.mission_types;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BreakMission implements ItemMissionType {

    private static final Random RAND = new Random();
    private static final List<String> TITLES = Arrays.asList(
            "Block Buster Bonanza",
            "Pixelated Demolition Derby",
            "Minecrafty Mayhem",
            "Terrain Takedown Titan",
            "Debris Dozer Dynamo"
    );
    @Override
    public String id() {
        return "break";
    }

    @Override
    public String difficulty() {
        return "easy";
    }

    public Component titleString() {
        String title = TITLES.get(RAND.nextInt(TITLES.size()));
        return Component.nullToEmpty(title);
    }

    @Override
    public Component taskString(String language, int progress, Component itemName) {
        return Component.nullToEmpty("Break mission");
    }
}
