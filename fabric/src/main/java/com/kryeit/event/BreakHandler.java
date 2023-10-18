package com.kryeit.event;

import com.kryeit.MissionHandler;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.BreakMission;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BreakHandler implements PlayerBlockBreakEvents.After {

    @Override
    public void afterBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (MissionHandler.isNotServerPlayer(player)) return;
        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        MissionManager.incrementMission(player.getUUID(), BreakMission.class, block, 1);
    }
}
