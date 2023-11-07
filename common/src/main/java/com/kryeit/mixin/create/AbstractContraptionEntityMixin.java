package com.kryeit.mixin.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.kryeit.Main.cachedTrainPlayerPositions;

@Mixin(value = AbstractContraptionEntity.class)
public class AbstractContraptionEntityMixin {

    @Inject(method = "getDismountLocationForPassenger", at = @At("HEAD"))
    private void onDismount(LivingEntity entityLiving, CallbackInfoReturnable<Vec3> cir) {
        if (entityLiving instanceof ServerPlayer user) {
            if (cachedTrainPlayerPositions.isEmpty()) return;
            for (ServerPlayer player : cachedTrainPlayerPositions.keySet()) {
                if (player.getUUID().equals(user.getUUID())) {
                    cachedTrainPlayerPositions.remove(player);
                }
            }
        }
    }
}
