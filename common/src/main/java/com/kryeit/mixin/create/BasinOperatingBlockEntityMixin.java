package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.basin.CompactMission;
import com.kryeit.missions.mission_types.create.basin.MixMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public class BasinOperatingBlockEntityMixin {
    @Shadow
    protected Recipe<?> currentRecipe;

    @Inject(method = "applyBasinRecipe", at = @At("RETURN"))
    private void onApplyBasinRecipe(CallbackInfo ci) {
        if (currentRecipe != null) {

            BlockEntityAccessor accessor = (BlockEntityAccessor) this;

            if (currentRecipe instanceof MixingRecipe)
                MixinUtils.handleMixinMissionItem(accessor, MixMission.class, currentRecipe.getResultItem(RegistryAccess.EMPTY));

            if (MixinUtils.isCompactingRecipe(currentRecipe))
                MixinUtils.handleMixinMissionItem(accessor, CompactMission.class, currentRecipe.getResultItem(RegistryAccess.EMPTY));
        }
    }
}
