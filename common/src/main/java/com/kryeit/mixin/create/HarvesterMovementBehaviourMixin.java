package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.create.contraption.HarvestMission;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HarvesterMovementBehaviour.class, remap = false)
public class HarvesterMovementBehaviourMixin {
    @Inject(
            method = "cutCrop",
            at = @At("HEAD")
    )
    private void cutCrop(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir) {

        Player closestPlayer = MixinUtils.getClosestPlayer(level, pos);

        ItemStack result = state.getBlock().asItem().getDefaultInstance();

        if (closestPlayer != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), HarvestMission.class, BuiltInRegistries.ITEM.getKey(result.getItem()),
                    1);
        }
    }
}
