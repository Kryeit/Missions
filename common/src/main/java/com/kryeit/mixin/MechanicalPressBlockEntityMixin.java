package com.kryeit.mixin;

import com.kryeit.Utils;
import com.kryeit.missions.mission_types.PressMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
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
    public void onItemPressed(ItemStack result, CallbackInfo ci) {

        BlockEntityAccessor accessor = (BlockEntityAccessor) this;
        Utils.handleMixinMissionItem(accessor, PressMission.class, result);
    }
}

