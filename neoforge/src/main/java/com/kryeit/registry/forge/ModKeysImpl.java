package com.kryeit.registry.forge;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;


// Code from https://github.com/Layers-of-Railways/Railway
public class ModKeysImpl {
    private static final List<KeyMapping> KEYBINDS = new ArrayList<>();

    public static void registerKeyBinding(KeyMapping keyMapping) {
        KEYBINDS.add(keyMapping);
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        for (KeyMapping keyMapping : KEYBINDS) {
            event.register(keyMapping);
        }
    }

    public static int getBoundCode(KeyMapping keyMapping) {
        return keyMapping.getKey().getValue();
    }
}