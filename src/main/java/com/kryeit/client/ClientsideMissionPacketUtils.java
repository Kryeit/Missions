package com.kryeit.client;

import com.kryeit.network.MissionPayloads;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;

/**
 * Client-side helper for talking to the server about missions. Sends are done through NeoForge's
 * {@link PacketDistributor}; incoming data is routed to whatever screen registered an update handler.
 */
public class ClientsideMissionPacketUtils {
    private static Consumer<ClientMissionData> updateHandler;

    public static void setMissionUpdateHandler(Consumer<ClientMissionData> handler) {
        updateHandler = handler;
    }

    public static void accept(ClientMissionData data) {
        if (updateHandler != null) {
            updateHandler.accept(data);
        }
    }

    public static void requestMissions() {
        PacketDistributor.sendToServer(new MissionPayloads.RequestMissions());
    }

    public static void requestReroll(int index) {
        PacketDistributor.sendToServer(new MissionPayloads.Reroll(index));
    }

    public static void requestPayout() {
        PacketDistributor.sendToServer(new MissionPayloads.Payout());
    }
}
