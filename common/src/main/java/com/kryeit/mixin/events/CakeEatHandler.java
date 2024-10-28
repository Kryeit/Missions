package com.kryeit.mixin.events;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.EatMission;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CakeBlock.class)
public class CakeEatHandler {

    @Inject(method = "eat", at = @At("HEAD"))
    private static void onEat(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(player instanceof ServerPlayer)) return;
        MissionManager.incrementMission(player.getUUID(), EatMission.class, new ResourceLocation("minecraft", "cake"), 1);
    }
}
