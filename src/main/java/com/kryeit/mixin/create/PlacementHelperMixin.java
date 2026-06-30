package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.PlaceMission;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlacementOffset.class, remap = false)
public class PlacementHelperMixin {


    @Inject(method = "placeInWorld", at = @At("RETURN"))
    private void place(Level world, BlockItem blockItem, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir) {
        if (world.isClientSide) return;

        MissionManager.incrementMission(player.getUUID(), PlaceMission.class, BuiltInRegistries.ITEM.getKey(blockItem.asItem()), 1);
    }
}
