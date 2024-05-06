package com.kryeit.missions;

import com.kryeit.registry.ModStats;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum MissionDifficulty {
    EASY(0x7ac142, Components.translatable("missions.difficulty.easy"), ModStats.EASY_MISSIONS_COMPLETED),
    NORMAL(0x66c8c9, Components.translatable("missions.difficulty.normal"), ModStats.NORMAL_MISSIONS_COMPLETED),
    HARD(0xd49250, Components.translatable("missions.difficulty.hard"), ModStats.HARD_MISSIONS_COMPLETED);

    private final int color;
    private final Component description;
    private final ResourceLocation stat;

    MissionDifficulty(int color, Component description, ResourceLocation stat) {
        this.color = color;
        this.description = description;
        this.stat = stat;
    }

    public int color() {
        return color;
    }

    public Component description() {
        return description;
    }

    public ResourceLocation stat() {
        return stat;
    }
}
