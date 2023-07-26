package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.Utils;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.CraftMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.RecipeGridHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MechanicalCrafterBlockEntity.class)
public class MechanicalCrafterBlockEntityMixin {

    @Shadow
    protected RecipeGridHandler.GroupedItems groupedItems;

    @Shadow
    private ItemStack scriptedResult;

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler;tryToApplyRecipe(Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/crafter/RecipeGridHandler$GroupedItems;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0, shift = At.Shift.AFTER))
    private void onCraftingEnd(CallbackInfo ci) {
        MechanicalCrafterBlockEntity blockEntity = (MechanicalCrafterBlockEntity) (Object) this;
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        Level level = accessor.getLevel();
        BlockPos worldPosition = accessor.getWorldPosition();

        ItemStack result =
                blockEntity.isVirtual() ? scriptedResult : RecipeGridHandler.tryToApplyRecipe(level, groupedItems);

        Player closestPlayer = null;

        if(level != null && worldPosition != null)
            closestPlayer = Utils.getClosestPlayer(level, worldPosition);

        if (closestPlayer != null)
            MissionTypeRegistry.INSTANCE.getType(CraftMission.class).handleItem(
                    closestPlayer.getUUID(),
                    PlatformSpecific.getResourceLocation(result.getItem()),
                    result.getCount());
    }
}
