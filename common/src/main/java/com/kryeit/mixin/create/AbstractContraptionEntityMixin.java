package com.kryeit.mixin.create;

import com.kryeit.Main;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractContraptionEntity.class, remap = false)
public class AbstractContraptionEntityMixin {

    @Inject(method = "getDismountLocationForPassenger", at = @At("HEAD"))
    private void onDismount(LivingEntity entityLiving, CallbackInfoReturnable<Vec3> cir) {
        if (entityLiving instanceof ServerPlayer user) {
            for (ServerPlayer player : Main.cachedTrainPlayerPositions.keySet()) {
                if (player.getUUID().equals(user.getUUID())) {
                    Main.cachedTrainPlayerPositions.remove(player);
                    return;
                }
            }
        }
    }
}
