package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.ChainConveyorRideMission;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.chainConveyor.ServerChainConveyorHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Accumulates the distance a player travels while hanging on a Create 6 chain conveyor with a wrench in
 * hand, and feeds it to {@link ChainConveyorRideMission}. Create tracks hanging players server-side in
 * {@code ServerChainConveyorHandler.hangingPlayers}, so no Create-side hook is needed.
 */
@Mixin(ServerPlayer.class)
public class ChainConveyorRideMixin {

    @Unique
    private static final Map<UUID, Vec3> missions$lastPositions = new HashMap<>();

    @Unique
    private static final Map<UUID, Double> missions$accumulatedDistances = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void missions$chainRideTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        UUID uuid = player.getUUID();

        boolean riding = ServerChainConveyorHandler.hangingPlayers.containsKey(uuid);
        boolean holdingWrench = player.getMainHandItem().is(AllItems.WRENCH.get())
                || player.getOffhandItem().is(AllItems.WRENCH.get());

        if (!riding || !holdingWrench) {
            missions$lastPositions.remove(uuid);
            return;
        }

        Vec3 current = player.position();
        Vec3 last = missions$lastPositions.get(uuid);

        if (last != null) {
            double distance = current.distanceTo(last);
            if (distance > 0.01) {
                double total = missions$accumulatedDistances.getOrDefault(uuid, 0.0) + distance;
                if (total >= 1.0) {
                    int fullBlocks = (int) total;
                    ChainConveyorRideMission.handleDistanceChange(uuid, fullBlocks);
                    total -= fullBlocks;
                }
                missions$accumulatedDistances.put(uuid, total);
            }
        }

        missions$lastPositions.put(uuid, current);
    }
}
