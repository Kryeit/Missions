package com.kryeit.event;

import com.kryeit.MissionHandler;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.PlaceMission;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BlockPlaceHandler implements BlockEvents.AfterPlace {
    @Override
    public void afterPlace(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (MissionHandler.isNotServerPlayer(player)) return;
        BlockPos pos = context.getClickedPos();
        ResourceLocation item = context.getLevel().getBlockState(pos).getBlock().getRegistryName();
        MissionManager.incrementMission(player.getUUID(), PlaceMission.class, item, 1);
    }
}
