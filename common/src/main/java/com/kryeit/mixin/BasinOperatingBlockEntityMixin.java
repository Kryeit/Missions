package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.Utils;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.MixingMission;
import com.kryeit.missions.mission_types.PressMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public class BasinOperatingBlockEntityMixin {
    @Shadow
    protected Recipe<?> currentRecipe;

    @Inject(method = "applyBasinRecipe", at = @At("RETURN"))
    private void onApplyBasinRecipe(CallbackInfo ci) {
        if (currentRecipe != null) {

            BlockEntityAccessor accessor = (BlockEntityAccessor) this;
            Level level = accessor.getLevel();
            BlockPos worldPosition = accessor.getWorldPosition();
            Player closestPlayer = null;

            closestPlayer = Utils.getClosestPlayer(level, worldPosition);
            if (currentRecipe instanceof MixingRecipe && closestPlayer != null) {
                MissionTypeRegistry.INSTANCE.getType(MixingMission.class).handleItem(
                        closestPlayer.getUUID(),
                        PlatformSpecific.getResourceLocation(currentRecipe.getResultItem().getItem()),
                        currentRecipe.getResultItem().getCount());
            }

        }
    }
}
