package com.kryeit.missions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionTypeRegistry {
    public static final MissionTypeRegistry INSTANCE = new MissionTypeRegistry();
    private final Map<String, MissionType> typesById = new HashMap<>();
    private final Map<Class<? extends MissionType>, MissionType> typesByClass = new HashMap<>();

    public void register(MissionType type) {
        typesById.put(type.id(), type);
        typesByClass.put(type.getClass(), type);
    }

    public MissionType getType(String id) {
        return typesById.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends MissionType> T getType(Class<T> clazz) {
        return (T) typesByClass.get(clazz);
    }

    public List<MissionType> getAllTypes() {
        return List.copyOf(typesById.values());
    }
}
