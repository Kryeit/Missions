package com.kryeit.mixin;

import com.kryeit.missions.mission_types.create.MillMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MillstoneBlockEntity.class, remap = false)
public class MillstoneBlockEntityMixin {

    @Shadow
    public ItemStackHandler outputInv;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/millstone/MillstoneBlockEntity;process()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void beforeProcess(CallbackInfo ci) {

        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        for(int i = 0; i < outputInv.getSlots().size(); i++) {
            ItemStack result = outputInv.getStackInSlot(i);
            if(result.getItem() == Items.AIR) continue;
            MixinUtils.handleMixinMissionItem(accessor, MillMission.class, result);
        }
    }
}
