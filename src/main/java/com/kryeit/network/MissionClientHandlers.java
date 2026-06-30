package com.kryeit.network;

import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.MissionScreen;
import com.kryeit.client.screen.toasts.MissionCompletedToast;
import net.minecraft.client.Minecraft;

/**
 * Client-side sink for the server to client Missions payloads. Only ever invoked on the client
 * (from the {@code playToClient} handlers in {@link MissionNetworking}), so it may freely reference
 * client-only classes.
 */
public final class MissionClientHandlers {
    private MissionClientHandlers() {}

    public static void openMissionScreen() {
        Minecraft.getInstance().setScreen(new MissionScreen());
    }

    public static void acceptData(ClientMissionData data) {
        ClientsideMissionPacketUtils.accept(data);
    }

    public static void showToast(ClientMissionData.ClientsideActiveMission mission) {
        MissionCompletedToast.show(mission);
    }
}
