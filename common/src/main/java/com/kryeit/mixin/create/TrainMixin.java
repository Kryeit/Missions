package com.kryeit.mixin.create;

import com.kryeit.Main;
import com.kryeit.missions.mission_types.train.TrainDriverMissionType;
import com.kryeit.missions.mission_types.train.TrainDriverPassengerMissionType;
import com.kryeit.missions.mission_types.train.TrainPassengerMissionType;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Train.class, remap = false)

public abstract class TrainMixin {

    @Shadow public double speed;

    @Shadow public List<Carriage> carriages;

    @Shadow public abstract int countPlayerPassengers();

    @Inject(method = "collideWithOtherTrains", at = @At("HEAD"))
    public void onMovementAllowed(Level level, Carriage car, CallbackInfo ci){
        if (Math.abs(speed) < 0.1)
            return;

        for (Carriage carriage : carriages) {
            carriage.forEachPresentEntity(e -> e.getIndirectPassengers()
                    .forEach(p -> {
                        if (p instanceof ServerPlayer player) {
                            if (!Main.cachedTrainPlayerPositions.containsKey(player)) Main.cachedTrainPlayerPositions.put(player, player.position());
                            double distance = MixinUtils.getDistance(Main.cachedTrainPlayerPositions.get(player), player.position());
                            if (distance < 50) return;

                            TravellingPoint trailingPoint = carriage.getTrailingPoint();
                            TravellingPoint leadingPoint = carriage.getLeadingPoint();

                            if (leadingPoint.node1 == null || trailingPoint.node1 == null)
                                return;
                            ResourceKey<Level> dimension = leadingPoint.node1.getLocation().dimension;
                            if (!dimension.equals(trailingPoint.node1.getLocation().dimension)) {
                                Main.cachedTrainPlayerPositions.replace(player, player.position());
                                return;
                            }

                            Main.cachedTrainPlayerPositions.replace(player, player.position());

                            if (e.getControllingPlayer().isPresent() && e.getControllingPlayer().get().equals(player.getUUID())) {
                                TrainDriverMissionType.handleDistanceChange(player.getUUID(), (int) distance);

                                if (TrainDriverPassengerMissionType.passengersNeeded() >= countPlayerPassengers()) {
                                    TrainDriverPassengerMissionType.handleDistanceChange(player.getUUID(), (int) distance);
                                }
                            } else {
                                TrainPassengerMissionType.handleDistanceChange(player.getUUID(), (int) distance);
                            }
                        }
                    }));
        }








        //if (MixinUtils.getDistance(start, end) < 50) return;


    }
}
