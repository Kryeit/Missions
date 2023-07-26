package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.Utils;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.CrushingMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrushingWheelControllerBlockEntity.class, remap = false)
public abstract class CrushingWheelControllerBlockEntityMixin {

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
    private void afterApplyRecipe(CallbackInfo ci) {

        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        for(int i = 1; i < inventory.getSlots(); i++) {
            ItemStack result = inventory.getStackInSlot(i);
            if(result.getItem() == Items.AIR) continue;
            Utils.handleMixinMission(accessor, CrushingMission.class, result);
        }
    }
}

