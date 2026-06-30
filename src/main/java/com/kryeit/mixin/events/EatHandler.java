package com.kryeit.mixin.events;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.EatMission;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class EatHandler {

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void onEat(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide) return;

        MissionManager.incrementMission(livingEntity.getUUID(), EatMission.class,
                BuiltInRegistries.ITEM.getKey(((ItemStack) (Object) this).getItem()),
                1);
    }
}
