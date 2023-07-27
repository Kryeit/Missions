package com.kryeit.mixin;

import com.kryeit.Utils;
import com.kryeit.missions.mission_types.create.basin.CompactingMission;
import com.kryeit.missions.mission_types.create.basin.MixingMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Only tested Forge side (seeing Fabric's code, seems the same as Forge's)
// https://github.com/Fabricators-of-Create/Create/blob/mc1.18/fabric/dev/src/main/java/com/simibubi/create/content/processing/basin/BasinOperatingBlockEntity.java#L99

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public class BasinOperatingBlockEntityMixin {
    @Shadow
    protected Recipe<?> currentRecipe;

    @Inject(method = "applyBasinRecipe", at = @At("RETURN"))
    private void onApplyBasinRecipe(CallbackInfo ci) {
        if (currentRecipe != null) {

            BlockEntityAccessor accessor = (BlockEntityAccessor) this;

            if (currentRecipe instanceof MixingRecipe)
                Utils.handleMixinMissionItem(accessor, MixingMission.class, currentRecipe.getResultItem());

            if(Utils.isCompactingRecipe(currentRecipe))
                Utils.handleMixinMissionItem(accessor, CompactingMission.class, currentRecipe.getResultItem());
            // TODO: Maybe compat for some Create addon that has a RecipeType that utilizes a basin 2 blocks below?
        }
    }
}
