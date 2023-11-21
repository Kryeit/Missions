package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.vanilla.CraftMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.RecipeGridHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MechanicalCrafterBlockEntity.class, remap = false)
public class MechanicalCrafterBlockEntityMixin {
    @Shadow
    protected RecipeGridHandler.GroupedItems groupedItems;

    @Inject(method = "continueIfAllPrecedingFinished", at = @At("HEAD"))
    private void onApplyMechanicalCraftingRecipe(CallbackInfo ci) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        ItemStack result =
                RecipeGridHandler.tryToApplyRecipe(accessor.getLevel(), groupedItems);
        if (result != null)
            MixinUtils.handleMixinMissionItem(accessor, CraftMission.class, result);
    }
}
