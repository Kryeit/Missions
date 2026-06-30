package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.train.TrainRelocateMission;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainRelocationPacket;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

// Create 6 (NeoForge 1.21.1) packet handler is handle(ServerPlayer) -> void.
@Mixin(value = TrainRelocationPacket.class, remap = false)
public class TrainRelocationPacketMixin {

    @Shadow
    UUID trainId;

    @Shadow
    BlockPos pos;

    @Inject(method = "handle", at = @At("HEAD"))
    private void onHandle(ServerPlayer player, CallbackInfo ci) {
        if (player == null)
            return;

        Train train = Create.RAILWAYS.trains.get(trainId);
        if (train == null)
            return;

        BlockPos from = train.carriages.stream().findFirst().get().anyAvailableEntity().blockPosition();
        BlockPos to = pos;

        int distance = (int) Math.sqrt(from.distSqr(to));

        if (distance > AllConfigs.server().trains.maxTrackPlacementLength.get() * 2) return;
        TrainRelocateMission.handleDistanceChange(player.getUUID(), distance);
    }

}
