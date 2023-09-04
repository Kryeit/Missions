package com.kryeit.missions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum MissionDifficulty {
    EASY(0xFFFFFF, new TranslatableComponent("missions.difficulty.easy")),
    NORMAL(0x0080FF, new TranslatableComponent("missions.difficulty.normal")),
    HARD(0x800080, new TranslatableComponent("missions.difficulty.hard"));

    private final int color;
    private final Component description;

    MissionDifficulty(int color, Component description) {
        this.color = color;
        this.description = description;
    }

    public int color() {
        return color;
    }

    public Component description() {
        return description;
    }
}
