package com.kryeit.missions.utils;

import java.util.Random;

public class Range {
    private final int lower;
    private final int upper;

    private Range(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public static Range of(int value1, int value2) {
        return new Range(Math.min(value1, value2), Math.max(value1, value2));
    }

    public int upper() {
        return upper;
    }

    public int lower() {
        return lower;
    }

    public int getRandomValue() {
        return new Random().nextInt(lower, upper + 1);
    }

    public static Range fromString(String s) {
        String[] split = s.split("-");
        int int1 = Integer.parseInt(split[0]);
        int int2 = Integer.parseInt(split[1]);
        return Range.of(int1, int2);
    }
}
