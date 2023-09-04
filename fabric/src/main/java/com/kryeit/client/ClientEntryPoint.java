package com.kryeit.client;

import com.kryeit.client.screen.MissionScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;


public class ClientEntryPoint implements ClientModInitializer {

    public static KeyMapping missionGuiKey;

    @Override
    public void onInitializeClient() {
        missionGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("missions.menu.key", GLFW.GLFW_KEY_H, "missions.key.category"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (missionGuiKey.isDown() && client.screen == null) {
                client.setScreen(new MissionScreen());
            }
        });
    }
}
