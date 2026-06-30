package com.kryeit.client;

import com.kryeit.Missions;
import com.kryeit.client.screen.MissionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Per-tick poll that opens the custom Missions screen when the keybind is pressed. Game event bus,
 * client only. {@code consumeClick()} already debounces, so no manual cooldown is needed.
 */
@EventBusSubscriber(modid = Missions.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class MissionsClientTick {
    private MissionsClientTick() {}

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
        if (mc.screen != null) return;

        while (MissionsKeyMappings.MISSIONS_MENU.consumeClick()) {
            mc.setScreen(new MissionScreen());
        }
    }
}
