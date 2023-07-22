package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KillMission implements ItemMissionType {

    private static final Random RAND = new Random();
    private static final List<String> TITLES = Arrays.asList(
            "Plague Extermination",
            "Make Doomsday Happen",
            "Creature Carnage Carnival",
            "Bizarre Beast Busting",
            "Monstrous Mayhem Maker"
    );
    @Override
    public String id() {
        return "kill";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    public Component titleString() {
        String title = TITLES.get(RAND.nextInt(TITLES.size()));
        return Component.nullToEmpty(title);
    }

    @Override
    public Component taskString(String language, int progress, Component itemName) {
        return Component.nullToEmpty("Kill mission");
    }
}
