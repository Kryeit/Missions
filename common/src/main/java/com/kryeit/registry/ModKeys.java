package com.kryeit.registry;

import com.kryeit.Missions;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;


// Code from https://github.com/Layers-of-Railways/Railway
public enum ModKeys {
    MISSIONS_MENU("missions_menu", GLFW.GLFW_KEY_H)
    ;

    private KeyMapping keybind;
    private final String description;
    private final int key;
    private final boolean modifiable;

    // Needed to make keys not appear to conflict between Steam 'n' Rails & Create
    public static final Set<KeyMapping> NON_CONFLICTING_KEYMAPPINGS = new HashSet<>();

    ModKeys(String description, int defaultKey) {
        this.description = Missions.MOD_ID + ".keyinfo." + description;
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    public static void register() {
        for (ModKeys key : values()) {
            if (!key.modifiable)
                continue;
            key.keybind = new KeyMapping(key.description, key.key, Missions.NAME);
            NON_CONFLICTING_KEYMAPPINGS.add(key.keybind);
            registerKeyBinding(key.keybind);
        }
    }

    public static void fixBinds() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        for (ModKeys key : values()) {
            if (key.keybind == null || key.keybind.isUnbound())
                continue;
            key.keybind.setDown(InputConstants.isKeyDown(window, key.getBoundCode()));
        }
    }

    public KeyMapping getKeybind() {
        return keybind;
    }

    public boolean isPressed() {
        if (!modifiable)
            return isKeyDown(key);
        return keybind.isDown();
    }

    public String getBoundKey() {
        return keybind.getTranslatedKeyMessage()
                .getString()
                .toUpperCase();
    }

    public int getBoundCode() {
        return getBoundCode(keybind);
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key);
    }

    public static boolean isMouseButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Minecraft.getInstance()
                .getWindow()
                .getWindow(), button) == 1;
    }

    public static boolean ctrlDown() {
        return Screen.hasControlDown();
    }

    public static boolean shiftDown() {
        return Screen.hasShiftDown();
    }

    public static boolean altDown() {
        return Screen.hasAltDown();
    }

    @ExpectPlatform
    private static void registerKeyBinding(KeyMapping keyMapping) {
        throw new AssertionError();
    }

    @ExpectPlatform
    private static int getBoundCode(KeyMapping keyMapping) {
        throw new AssertionError();
    }
}
