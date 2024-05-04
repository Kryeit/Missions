package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.belt.BeltWalkMission;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kryeit.Missions.cachedBeltPlayerPositions;

@Mixin(BeltMovementHandler.class)
public class BeltMovementHandlerMixin {

    @Inject(method = "transportEntity(Lcom/simibubi/create/content/kinetics/belt/BeltBlockEntity;Lnet/minecraft/world/entity/Entity;Lcom/simibubi/create/content/kinetics/belt/transport/BeltMovementHandler$TransportedEntityInfo;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"))
    private static void onBeltTransportEntity(BeltBlockEntity beltBE, Entity entityIn, BeltMovementHandler.TransportedEntityInfo info, CallbackInfo ci) {

        if (entityIn instanceof ServerPlayer player) {
            double distance = MixinUtils.getDistance(cachedBeltPlayerPositions.get(player), player.position());

            if (!cachedBeltPlayerPositions.containsKey(player) || distance > 10 || info.getTicksSinceLastCollision() > 20) {
                cachedBeltPlayerPositions.put(player, player.position());
                return;
            }

            if (distance < 5) return;

            cachedBeltPlayerPositions.replace(player, player.position());

            BeltWalkMission.handleDistanceChange(player.getUUID(), 5);
        }
    }
}
