package com.kryeit.forge.client;

import com.kryeit.Main;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public final class KeyInit {
    public static KeyMapping missionGuiKey;

    private KeyInit() {
    }

    public static void init() {
        missionGuiKey = registerKey("mission_gui", "key.category.missions", InputConstants.KEY_H);
    }

    private static KeyMapping registerKey(String name, String category, int keycode) {
        final var key = new KeyMapping("key." + Main.MOD_ID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}

