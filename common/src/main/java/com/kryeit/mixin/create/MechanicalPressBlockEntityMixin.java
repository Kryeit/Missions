package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.PressMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Tested Fabric and Forge side

@Mixin(value = MechanicalPressBlockEntity.class, remap = false)
public class MechanicalPressBlockEntityMixin {

    @Inject(method = "onItemPressed", at = @At("RETURN"))
    public void onApplyPressingRecipe(ItemStack result, CallbackInfo ci) {

        MixinUtils.handleMixinMissionItem(
                (BlockEntityAccessor) this,
                PressMission.class,
                result);
    }
}

