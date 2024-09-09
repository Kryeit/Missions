package com.kryeit.registry.fabric;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;


// Code from https://github.com/Layers-of-Railways/Railway
public class ModKeysImpl {
    public static void registerKeyBinding(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    public static int getBoundCode(KeyMapping keyMapping) {
        return KeyBindingHelper.getBoundKeyOf(keyMapping).getValue();
    }
}
