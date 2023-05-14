package com.kryeit.fabric.client;

import com.mojang.brigadier.LiteralMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;


public class ClientEntryPoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMapping missionGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mission_gui",  GLFW.GLFW_KEY_H, "key.category.missions"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(missionGuiKey.isDown()) {
                client.player.sendMessage(new TranslatableComponent("test"),client.player.getUUID());
            }
        });
    }
}
