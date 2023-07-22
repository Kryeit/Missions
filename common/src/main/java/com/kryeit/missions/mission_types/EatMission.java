package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EatMission implements ItemMissionType {

    private static final Random RAND = new Random();
    private static final List<String> TITLES = Arrays.asList(
            "Chomp Chomp Champion",
            "Gobble Gobble Gladiator",
            "Epicurean Expedition",
            "Feast of Frenzy",
            "Culinary Conqueror"
    );
    @Override
    public String id() {
        return "eat";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.NORMAL;
    }

    public Component titleString() {
        String title = TITLES.get(RAND.nextInt(TITLES.size()));
        return Component.nullToEmpty(title);
    }

    @Override
    public Component taskString(String language, int progress, Component itemName) {
        return Component.nullToEmpty("Eat mission");
    }
}
