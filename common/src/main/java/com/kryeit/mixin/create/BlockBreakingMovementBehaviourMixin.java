package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.create.DrillMission;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockBreakingMovementBehaviour.class, remap = false)
public class BlockBreakingMovementBehaviourMixin {

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    protected void destroyBlock(MovementContext context, BlockPos breakingPos, CallbackInfo ci) {

        Level level = context.world;
        Player closestPlayer = MixinUtils.getClosestPlayer(level, breakingPos);

        ItemStack result = context.world.getBlockState(breakingPos).getBlock().asItem().getDefaultInstance();

        if (closestPlayer != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), DrillMission.class, BuiltInRegistries.ITEM.getKey(result.getItem()),
                    1);
        }
    }
}
