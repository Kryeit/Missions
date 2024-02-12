package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.train.TrainDriverMissionType;
import com.kryeit.missions.mission_types.create.train.TrainDriverPassengerMissionType;
import com.kryeit.missions.mission_types.create.train.TrainPassengerMissionType;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.kryeit.Missions.cachedTrainPlayerPositions;
import static com.kryeit.missions.mission_types.create.train.TrainDriverPassengerMissionType.passengersNeeded;

@Mixin(value = Train.class, remap = false)
public abstract class TrainMixin {

    @Shadow public double speed;

    @Shadow public List<Carriage> carriages;

    @Shadow public abstract int countPlayerPassengers();

    @Inject(method = "collideWithOtherTrains", at = @At("HEAD"))
    public void onTrainMove(Level level, Carriage car, CallbackInfo ci){
        if (Math.abs(speed) < 0.1)
            return;

        for (Carriage carriage : carriages) {
            carriage.forEachPresentEntity(e -> e.getIndirectPassengers()
                    .forEach(p -> {
                        if (p instanceof ServerPlayer player) {
                            double distance = MixinUtils.getDistance(cachedTrainPlayerPositions.get(player), player.position());

                            if (!cachedTrainPlayerPositions.containsKey(player) || distance > 100) {
                                cachedTrainPlayerPositions.put(player, player.position());
                                return;
                            }

                            if (distance < 50) return;

                            cachedTrainPlayerPositions.replace(player, player.position());

                            if (e.getControllingPlayer().isPresent() && e.getControllingPlayer().get().equals(player.getUUID())) {
                                TrainDriverMissionType.handleDistanceChange(player.getUUID(), 50);

                                if (passengersNeeded() <= countPlayerPassengers() - 1) {
                                    TrainDriverPassengerMissionType.handleDistanceChange(player.getUUID(), 50);
                                }
                            } else {
                                TrainPassengerMissionType.handleDistanceChange(player.getUUID(), 50);
                            }
                        }
                    }));
        }
    }
}
