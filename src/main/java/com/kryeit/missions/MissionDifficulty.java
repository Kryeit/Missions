package com.kryeit.missions;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum MissionDifficulty {
    EASY(0x7ac142, "Easy", ChatFormatting.GREEN),
    NORMAL(0x66c8c9, "Normal", ChatFormatting.AQUA),
    HARD(0xd49250, "Hard", ChatFormatting.GOLD);

    private final int color;
    private final String displayName;
    private final ChatFormatting format;

    MissionDifficulty(int color, String displayName, ChatFormatting format) {
        this.color = color;
        this.displayName = displayName;
        this.format = format;
    }

    public int color() {
        return color;
    }

    /**
     * Literal description so vanilla clients (which lack this mod's lang files) render real text.
     */
    public Component description() {
        return Component.literal(displayName).withStyle(format);
    }

    public ChatFormatting format() {
        return format;
    }

    /**
     * Key used to persist per-difficulty completion counters in {@link DataStorage}
     * (replaces the old custom vanilla stats, which aren't safe to sync to vanilla clients).
     */
    public String statKey() {
        return "completed_" + name().toLowerCase();
    }
}
