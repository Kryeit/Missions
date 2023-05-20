package com.kryeit.missions.wrappers;

public interface PlayerData {
    long getLong(String key);

    void setLong(String key, long value);

    int getInt(String key);

    void setInt(String key, int value);
}
