package com.kryeit;

import com.kryeit.event.BlockBreakHandler;
import com.kryeit.event.BlockPlaceHandler;
import com.kryeit.event.EatHandler;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityUseItemEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MissionHandler {
    public static void missionHandler() {
        PlayerBlockBreakEvents.AFTER.register(new BlockBreakHandler());
        BlockEvents.AFTER_PLACE.register(new BlockPlaceHandler());
        LivingEntityUseItemEvents.LIVING_USE_ITEM_FINISH.register(new EatHandler());

    }

    public static boolean isNotServerPlayer(Player player) {
        return !(player instanceof ServerPlayer);
    }
}
