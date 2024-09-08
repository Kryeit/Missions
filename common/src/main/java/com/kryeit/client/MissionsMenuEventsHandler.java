package com.kryeit.client;

import com.kryeit.annotation.event.MultiLoaderEvent;
import com.kryeit.client.screen.MissionScreen;
import com.kryeit.registry.ModKeys;
import com.simibubi.create.foundation.gui.ScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class MissionsMenuEventsHandler {
    public static int COOLDOWN = 0;

    @MultiLoaderEvent
    public static void clientTick() {
        if (COOLDOWN > 0 && !ModKeys.MISSIONS_MENU.isPressed())
            COOLDOWN--;
    }

    @MultiLoaderEvent
    public static void onKeyInput(int key, boolean pressed) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        if (key != ModKeys.MISSIONS_MENU.getBoundCode() || !pressed)
            return;
        if (COOLDOWN > 0)
            return;
        LocalPlayer player = mc.player;
        if (player == null)
            return;

        ScreenOpener.open(new MissionScreen());
    }
}
