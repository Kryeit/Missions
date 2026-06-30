package com.kryeit.network;

import com.kryeit.Missions;
import com.kryeit.missions.MissionManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers the Missions payloads on the mod event bus. Server handlers mutate state on the main
 * thread via {@code ctx.enqueueWork}; client handlers delegate to {@link com.kryeit.network.MissionClientHandlers}
 * inside the enqueued task so that client-only class is never loaded on a dedicated server.
 */
@EventBusSubscriber(modid = Missions.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MissionNetworking {
    private MissionNetworking() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Client -> Server
        registrar.playToServer(MissionPayloads.RequestMissions.TYPE, MissionPayloads.RequestMissions.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> MissionManager.sendMissions((ServerPlayer) ctx.player())));

        registrar.playToServer(MissionPayloads.Payout.TYPE, MissionPayloads.Payout.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> MissionManager.giveReward((ServerPlayer) ctx.player())));

        registrar.playToServer(MissionPayloads.Reroll.TYPE, MissionPayloads.Reroll.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> {
                    if (payload.index() >= 0 && payload.index() < 10) {
                        MissionManager.tryReassignMission((ServerPlayer) ctx.player(), payload.index());
                        MissionManager.sendMissions((ServerPlayer) ctx.player());
                    }
                }));

        // Server -> Client
        registrar.playToClient(MissionPayloads.OpenMissions.TYPE, MissionPayloads.OpenMissions.CODEC,
                (payload, ctx) -> ctx.enqueueWork(MissionClientHandlers::openMissionScreen));

        registrar.playToClient(MissionPayloads.ActiveMissions.TYPE, MissionPayloads.ActiveMissions.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> MissionClientHandlers.acceptData(payload.data())));

        registrar.playToClient(MissionPayloads.ShowToast.TYPE, MissionPayloads.ShowToast.CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> MissionClientHandlers.showToast(payload.mission())));
    }
}
