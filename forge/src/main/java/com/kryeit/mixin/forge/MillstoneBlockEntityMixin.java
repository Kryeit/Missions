package com.kryeit.mixin.forge;

import com.kryeit.missions.mission_types.create.MillMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
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

        // Find a fix: This gives player more progress in missions if they dont output the results right away
        for (int i = 0; i < outputInv.getSlots(); i++) {
            ItemStack result = outputInv.getStackInSlot(i);
            if (result.getItem() == Items.AIR) continue;
            MixinUtils.handleMixinMissionItem(accessor, MillMission.class, result);
        }
    }
}