package com.kryeit.fabric.mixin;

import com.kryeit.Utils;
import com.kryeit.missions.mission_types.create.CrushMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MillstoneBlockEntity.class, remap = false)
public class MillstoneBlockEntityMixin {
    public ItemStackHandler outputInv;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/millstone/MillstoneBlockEntity;process()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void beforeProcess(CallbackInfo ci) {

        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        for(int i = 0; i < outputInv.getSlots(); i++) {
            ItemStack result = outputInv.getStackInSlot(i);
            if(result.getItem() == Items.AIR) continue;
            Utils.handleMixinMissionItem(accessor, CrushMission.class, result);
        }
    }
}
