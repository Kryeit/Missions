package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.Utils;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.CompactingMission;
import com.kryeit.missions.mission_types.MixingMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Only tested Forge side

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public class BasinOperatingBlockEntityMixin {
    @Shadow
    protected Recipe<?> currentRecipe;

    @Inject(method = "applyBasinRecipe", at = @At("RETURN"))
    private void onApplyBasinRecipe(CallbackInfo ci) {
        if (currentRecipe != null) {

            BlockEntityAccessor accessor = (BlockEntityAccessor) this;
            Player closestPlayer = Utils.getClosestPlayer(accessor.getLevel(), accessor.getWorldPosition());

            if (currentRecipe instanceof MixingRecipe && closestPlayer != null)
                MissionTypeRegistry.INSTANCE.getType(MixingMission.class).handleItem(
                        closestPlayer.getUUID(),
                        PlatformSpecific.getResourceLocation(currentRecipe.getResultItem().getItem()),
                        currentRecipe.getResultItem().getCount());

            if(Utils.isCompactingRecipe(currentRecipe) && closestPlayer != null)
                MissionTypeRegistry.INSTANCE.getType(CompactingMission.class).handleItem(
                        closestPlayer.getUUID(),
                        PlatformSpecific.getResourceLocation(currentRecipe.getResultItem().getItem()),
                        currentRecipe.getResultItem().getCount());
            // TODO: Maybe compat for some Create addon that has a RecipeType that utilizes a basin 2 blocks below?
        }
    }
}
