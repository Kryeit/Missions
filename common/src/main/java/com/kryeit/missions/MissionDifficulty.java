package com.kryeit.missions;

import com.simibubi.create.foundation.utility.Components;
import net.minecraft.network.chat.Component;

public enum MissionDifficulty {
    EASY(0x7ac142, Components.translatable("missions.difficulty.easy")),
    NORMAL(0x66c8c9, Components.translatable("missions.difficulty.normal")),
    HARD(0xd49250, Components.translatable("missions.difficulty.hard"));

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
