package com.kryeit.entry.forge;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class KeyInit {

    public static KeyMapping MISSION_GUI = new KeyMapping(
            "missions.menu.key",
            GLFW.GLFW_KEY_H,
            "missions.key.category"
    );
    public KeyInit() {}

}