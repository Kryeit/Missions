package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.missions.mission_types.create.contraption.DrillMission;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.kryeit.mixin.interfaces.TreeAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.foundation.utility.TreeCutter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simibubi.create.foundation.utility.TreeCutter.NO_TREE;
import static com.simibubi.create.foundation.utility.TreeCutter.findTree;

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

            BlockState logOrLeave = context.world.getBlockState(breakingPos);
            AtomicBoolean isLog = new AtomicBoolean(false);
            AtomicBoolean isLeave = new AtomicBoolean(false);

            logOrLeave.getTags().iterator().forEachRemaining(action -> {
                if (action.equals(BlockTags.LOGS)) {
                    isLog.set(true);
                } else if (action.equals(BlockTags.LEAVES)) {
                    isLeave.set(true);
                }
            });

            TreeCutter.Tree tree = findTree(context.world, breakingPos);
            if (!tree.equals(NO_TREE)) {
                TreeAccessor treeAccessor = (TreeAccessor) tree;
                if (isLog.get()) {
                    treeAccessor.getLogs().forEach(blockPos -> count.getAndIncrement());
                } else if (isLeave.get()) {
                    treeAccessor.getLeaves().forEach(blockPos -> count.getAndIncrement());
                }
            }
        } else {
            return;
        }

        Level level = context.world;
        Player closestPlayer = MixinUtils.getClosestPlayer(level, breakingPos);

        ItemStack result = context.world.getBlockState(breakingPos).getBlock().asItem().getDefaultInstance();

        if (closestPlayer != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), mission, BuiltInRegistries.ITEM.getKey(result.getItem()),
                    count.get());
        }
    }
}
