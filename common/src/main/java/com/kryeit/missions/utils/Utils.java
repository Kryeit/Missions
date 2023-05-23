package com.kryeit.missions.utils;

import java.util.Collection;

public class Utils {
    public static <T> T getRandomEntry(Collection<T> collection) {
        int num = (int) (Math.random() * collection.size());
        for(T t: collection) if (--num < 0) return t;
        throw new AssertionError();
    }
}
