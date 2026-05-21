package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.PlaceMission;
import com.simibubi.create.content.trains.track.TrackPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackPlacement.class)
public class TrackPlacementMixin {

    @Inject(method = "placeTracks", at = @At("RETURN"))
    private static void place(Level level, TrackPlacement.PlacementInfo info, BlockState state1, BlockState state2, BlockPos targetPos1, BlockPos targetPos2, boolean simulate, CallbackInfoReturnable<TrackPlacement.PlacementInfo> cir) {
        if (simulate) return;
        int tracksPlaced = info.requiredTracks;

        Player player = level.getNearestPlayer(targetPos1.getX(), targetPos1.getY(), targetPos1.getZ(), 10, false);
        if (player == null) return;

        MissionManager.incrementMission(player.getUUID(), PlaceMission.class, ResourceLocation.fromNamespaceAndPath("create", "track"), tracksPlaced);
    }
}
