package com.kryeit.entry.forge;

import com.kryeit.Main;
import com.kryeit.client.screen.MissionScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Main.MOD_ID)
public final class KeyInit {

    public static KeyMapping MISSION_GUI = new KeyMapping(
            "missions.menu.key",
            GLFW.GLFW_KEY_H,
            "missions.key.category"
    );
    public KeyInit() {}

}