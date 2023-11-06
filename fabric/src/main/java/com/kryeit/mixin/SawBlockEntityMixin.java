package com.kryeit.mixin;

import com.kryeit.missions.mission_types.create.CutMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SawBlockEntity.class, remap = false)
public class SawBlockEntityMixin {

    @Shadow
    public ProcessingInventory inventory;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/saw/SawBlockEntity;applyRecipe()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onApplyCuttingRecipe(CallbackInfo ci) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack result = inventory.getStackInSlot(i);
            if (result.getItem() == Items.AIR) continue;
            MixinUtils.handleMixinMissionItem(accessor, CutMission.class, result);
        }
    }
}
