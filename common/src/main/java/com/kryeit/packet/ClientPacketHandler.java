package com.kryeit.packet;

import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.toasts.MissionCompletedToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;


public class ClientPacketHandler {
    private static final Map<ResourceLocation, Consumer<FriendlyByteBuf>> handlers = Map.of(
            ClientsideMissionPacketUtils.IDENTIFIER, ClientsideMissionPacketUtils::handlePacket,
            ClientsideMissionPacketUtils.SHOW_TOAST, buf -> {
                ClientsideActiveMission mission = ClientsideActiveMission.fromBuffer(buf);
                MissionCompletedToast.show(mission);
            }
    );

    public static boolean handle(ResourceLocation identifier, FriendlyByteBuf data) {
        Consumer<FriendlyByteBuf> handler = handlers.get(identifier);
        if (handler == null) return false;
        handler.accept(data);
        return true;
    }
}
