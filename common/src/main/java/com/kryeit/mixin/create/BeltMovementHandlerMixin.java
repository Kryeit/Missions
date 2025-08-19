package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.belt.BeltWalkMission;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public class BeltMovementHandlerMixin {

    @Unique
    private static final Map<UUID, Vec3> lastPositions = new HashMap<>();

    @Unique
    private static final Map<UUID, Double> accumulatedDistances = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onPlayerTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        // Use getOnPos to properly detect belts even if slightly below player's feet
        BlockPos standingOn = player.getOnPos();
        BlockState state = player.level().getBlockState(standingOn);

        // Only track movement if the player is on a Create belt
        if (!AllBlocks.BELT.has(state)) return;

        UUID uuid = player.getUUID();
        Vec3 current = player.position();
        Vec3 last = lastPositions.get(uuid);

        if (last != null) {
            double distance = current.distanceTo(last);

            if (distance > 0.01) { // Only accumulate significant movement
                double total = accumulatedDistances.getOrDefault(uuid, 0.0) + distance;

                if (total >= 1.0) {
                    int fullBlocks = (int) total;
                    BeltWalkMission.handleDistanceChange(uuid, fullBlocks);
                    total -= fullBlocks;
                }

                accumulatedDistances.put(uuid, total);
            }
        }

        lastPositions.put(uuid, current);
    }
}
