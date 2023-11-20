package com.kryeit.mixin;

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

@Mixin(MechanicalCrafterBlockEntity.class)
public class MechanicalCrafterBlockEntityMixin {
    @Shadow(remap = false)
    protected RecipeGridHandler.GroupedItems groupedItems;
    @Shadow(remap = false)
    private ItemStack scriptedResult;
    @Shadow(remap = false)
    protected int countDown;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler;tryToApplyRecipe(Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler$GroupedItems;)Lnet/minecraft/world/item/ItemStack;"), remap = false)
    private void onApplyMechanicalCraftingRecipe(CallbackInfo ci) {

        if (countDown != 20)
            return;

        MechanicalCrafterBlockEntity blockEntity = (MechanicalCrafterBlockEntity) (Object) this;
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        ItemStack result =
                blockEntity.isVirtual() ? scriptedResult : RecipeGridHandler.tryToApplyRecipe(accessor.getLevel(), groupedItems);

        if (result != null)
            MixinUtils.handleMixinMissionItem(accessor, CraftMission.class, result);
    }
}
