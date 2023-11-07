package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.missions.mission_types.create.contraption.DrillMission;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = BlockBreakingMovementBehaviour.class, remap = false)
public class BlockBreakingMovementBehaviourMixin {

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    protected void destroyBlock(MovementContext context, BlockPos breakingPos, CallbackInfo ci) {
        Block block = context.state.getBlock();
        Class<? extends MultiResourceMissionType> mission;
        AtomicInteger count = new AtomicInteger(1);

        if (block instanceof DrillBlock) {
            mission = DrillMission.class;
        } else if (block instanceof SawBlock) {
            mission = SawMission.class;
        } else {
            return;
        }

        Level level = context.world;
        Player closestPlayer = MixinUtils.getClosestPlayer(level, breakingPos);

        ItemStack result = context.world.getBlockState(breakingPos).getBlock().asItem().getDefaultInstance();

        if (closestPlayer != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), mission, Registry.ITEM.getKey(result.getItem()),
                    count.get());
        }
    }
}
