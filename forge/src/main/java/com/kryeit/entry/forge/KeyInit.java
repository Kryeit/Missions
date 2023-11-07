package com.kryeit.entry.forge;

import com.kryeit.Main;
import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public final class KeyInit {

    public static final KeyMapping missionGuiKey = registerKey("mission.menu.key", "missions.key.category", InputConstants.KEY_H);
    private KeyInit() {
    }

    @SuppressWarnings("SameParameterValue")
    private static KeyMapping registerKey(String name, String category, int keycode) {
        KeyMapping key = new KeyMapping("key." + Main.MOD_ID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (missionGuiKey.isDown() && minecraft.screen == null) {
            minecraft.setScreen(new MissionScreen());
        }
    }

}
