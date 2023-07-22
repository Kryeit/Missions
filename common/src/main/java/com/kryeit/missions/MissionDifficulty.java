package com.kryeit.missions;

public enum MissionDifficulty {
    EASY(0xFFFFFF),
    NORMAL(0x0080FF),
    HARD(0x800080);

    private final int color;

    MissionDifficulty(int color) {
        this.color = color;
    }

    public int color() {
        return color;
    }
}
