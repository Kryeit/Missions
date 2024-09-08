package com.kryeit.events;

import com.kryeit.annotation.event.MultiLoaderEvent;
import com.kryeit.client.MissionsMenuEventsHandler;
import com.kryeit.registry.ModKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public class ClientEvents {

    @ApiStatus.Internal
    public static boolean previousDevCapeSetting = false;

    @MultiLoaderEvent
    public static void onClientTickStart(Minecraft mc) {
        ModKeys.fixBinds();

        Level level = mc.level;
        long ticks = level == null ? 1 : level.getGameTime();


        if (isGameActive()) {
            MissionsMenuEventsHandler.clientTick();
        }
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @MultiLoaderEvent
    public static void onClientTickEnd(Minecraft mc) {

    }

    @MultiLoaderEvent
    public static void onKeyInput(int key, boolean pressed) {
        if (Minecraft.getInstance().screen != null)
            return;
        MissionsMenuEventsHandler.onKeyInput(key, pressed);
    }
}
