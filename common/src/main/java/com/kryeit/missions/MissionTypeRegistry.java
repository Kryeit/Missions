package com.kryeit.missions;

import java.util.HashMap;
import java.util.Map;

public class MissionTypeRegistry {
    public static final MissionTypeRegistry INSTANCE = new MissionTypeRegistry();
    private final Map<String, MissionType> types = new HashMap<>();

    public void register(MissionType type) {
        types.put(type.id(), type);
    }

    public MissionType getType(String id) {
        return types.get(id);
    }

    public <T extends MissionType> T getType(Class<T> clazz) {
        for (MissionType value : types.values()) {
            if (value.getClass().equals(clazz)) {
                return (T) value;
            }
        }
        return null;
    }
}
