package com.kryeit.entry.forge;

import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public final class KeyInit {

    public static final Lazy<KeyMapping> MISSION_GUI = Lazy.of(() -> new KeyMapping(
            "missions.menu.key",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "missions.key.category"
    ));
    public KeyInit() {}

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(MISSION_GUI.get());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (MISSION_GUI.get().consumeClick()) {
                Minecraft.getInstance().setScreen(new MissionScreen());
            }
        }
    }

}
