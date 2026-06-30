package com.kryeit.client;

import com.kryeit.Missions;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Registers the client keybind that opens the custom Missions screen. Mod event bus, client only.
 */
@EventBusSubscriber(modid = Missions.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MissionsKeyMappings {
    private MissionsKeyMappings() {}

    public static final KeyMapping MISSIONS_MENU = new KeyMapping(
            "missions.keyinfo.missions_menu", GLFW.GLFW_KEY_H, "key.categories." + Missions.MOD_ID);

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(MISSIONS_MENU);
    }
}
