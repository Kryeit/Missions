package com.kryeit.packet;

import com.kryeit.missions.MissionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.kryeit.client.ClientsideMissionPacketUtils.*;

public class ServerPacketHandler {
    private static final Map<ResourceLocation, BiConsumer<ServerPlayer, FriendlyByteBuf>> handlers = Map.of(
            PAYOUT_IDENTIFIER, (player, buf) -> MissionManager.giveReward(player),
            REQUEST_MISSIONS, (player, buf) -> MissionManager.sendMissions(player),
            REROLL_IDENTIFIER, (player, buf) -> {
                int index = buf.readInt();
                if (index < 10) {
                    MissionManager.tryReassignMission(player, index);
                }
            }
    );

    public static boolean handle(ResourceLocation identifier, ServerPlayer player, FriendlyByteBuf data) {
        BiConsumer<ServerPlayer, FriendlyByteBuf> handler = handlers.get(identifier);
        if (handler == null) return false;
        handler.accept(player, data);
        return true;
    }
}
