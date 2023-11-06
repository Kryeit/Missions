package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.missions.mission_types.create.contraption.DrillMission;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.mixin.interfaces.TreeAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.foundation.utility.TreeCutter;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simibubi.create.foundation.utility.TreeCutter.NO_TREE;
import static com.simibubi.create.foundation.utility.TreeCutter.findTree;

@Mixin(value = BlockBreakingKineticBlockEntity.class, remap = false)
public abstract class BlockBreakingKineticBlockEntityMixin {
    @Shadow protected abstract BlockPos getBreakingPos();

    @Inject(method = "onBlockBroken", at = @At("HEAD"))
    public void onBlockBroken(BlockState stateToBreak, CallbackInfo ci) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        BlockBreakingKineticBlockEntity entity = (BlockBreakingKineticBlockEntity) (Object) this;
        Class<? extends MultiResourceMissionType> mission;
        AtomicInteger count = new AtomicInteger(1);

        BlockPos pos = getBreakingPos();
        BlockState state = accessor.getLevel().getBlockState(pos);
        ItemStack result = state.getBlock().asItem().getDefaultInstance();

        if (entity instanceof SawBlockEntity) {
            mission = SawMission.class;

            BlockState logOrLeave = accessor.getLevel().getBlockState(pos);
            AtomicBoolean isLog = new AtomicBoolean(false);
            AtomicBoolean isLeave = new AtomicBoolean(false);

            logOrLeave.getTags().iterator().forEachRemaining(action -> {
                if (action.equals(BlockTags.LOGS)) {
                    isLog.set(true);
                } else if (action.equals(BlockTags.LEAVES)) {
                    isLeave.set(true);
                }
            });

            TreeCutter.Tree tree = findTree(accessor.getLevel(), pos);
            if (!tree.equals(NO_TREE)) {
                TreeAccessor treeAccessor = (TreeAccessor) tree;
                if (isLog.get()) {
                    treeAccessor.getLogs().forEach(blockPos -> count.getAndIncrement());
                } else if (isLeave.get()) {
                    treeAccessor.getLeaves().forEach(blockPos -> count.getAndIncrement());
                }
            }
        } else if (entity instanceof DrillBlockEntity){
            mission = DrillMission.class;
        } else {
            return;
        }

        result.setCount(count.get());
        MixinUtils.handleMixinMissionItem(
                accessor,
                mission,
                result);
    }

}
