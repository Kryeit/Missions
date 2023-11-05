package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.missions.mission_types.create.CutMission;
import com.kryeit.missions.mission_types.create.DrillMission;
import com.kryeit.missions.mission_types.create.SawMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockBreakingKineticBlockEntity.class, remap = false)
public abstract class BlockBreakingKineticBlockEntityMixin {
    @Shadow protected abstract BlockPos getBreakingPos();

    @Inject(method = "onBlockBroken", at = @At("HEAD"))
    public void onBlockBroken(BlockState stateToBreak, CallbackInfo ci) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        BlockBreakingKineticBlockEntity entity = (BlockBreakingKineticBlockEntity) (Object) this;
        Class<? extends MultiResourceMissionType> mission;

        if (entity instanceof SawBlockEntity) {
            mission = SawMission.class;
        } else if (entity instanceof DrillBlockEntity){
            mission = DrillMission.class;
        } else {
            return;
        }

        BlockPos pos = getBreakingPos();
        BlockState state = accessor.getLevel().getBlockState(pos);
        ItemStack result = state.getBlock().asItem().getDefaultInstance();

        MixinUtils.handleMixinMissionItem(
                (BlockEntityAccessor) this,
                mission,
                result);
    }

}
