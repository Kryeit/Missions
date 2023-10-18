package com.kryeit.mixin.forge;

import com.kryeit.missions.mission_types.create.CrushMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = CrushingWheelControllerBlockEntity.class, remap = false)
public class CrushingWheelControllerBlockEntityMixin {

    @Shadow
    public ProcessingInventory inventory;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/crusher/CrushingWheelControllerBlockEntity;applyRecipe()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onApplyCrushingRecipe(CallbackInfo ci) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        for (int i = 1; i < inventory.getSlots(); i++) {
            ItemStack result = inventory.getStackInSlot(i);
            if (result.getItem() == Items.AIR) continue;
            MixinUtils.handleMixinMissionItem(accessor, CrushMission.class, result);
        }
    }
}

