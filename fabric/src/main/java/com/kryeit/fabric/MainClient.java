package com.kryeit.fabric;

import com.kryeit.client.screen.MissionScreen;
import com.kryeit.missions.config.ConfigReader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;


public class MainClient implements ClientModInitializer {

    public static KeyMapping missionGuiKey;

    @Override
    public void onInitializeClient() {
        missionGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("missions.menu.key", GLFW.GLFW_KEY_H, "missions.key.category"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (missionGuiKey.isDown() && client.screen == null) {
                client.setScreen(new MissionScreen());
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("missions")
                    .executes(context -> {
                        // This is made by ChatScreenMixin.java
                        //context.getSource().getClient().setScreen(new MissionScreen());
                        System.out.println(ConfigReader.EXCHANGER_DROP_RATE);
                        System.out.println(ConfigReader.FIRST_REROLLING_CURRENCY);

                        return 1;
                    }));
        });
    }
}
