package com.kryeit.missions;

public enum MissionDifficulty {
    EASY(0xFFFFFF, "Easy"),
    NORMAL(0x0080FF, "Normal"),
    HARD(0x800080, "Hard");

    private final int color;
    private final String description;

    MissionDifficulty(int color, String description) {
        this.color = color;
        this.description = description;
    }

    public int color() {
        return color;
    }


    @Override
    public String toString() {
        return description;
    }
}
