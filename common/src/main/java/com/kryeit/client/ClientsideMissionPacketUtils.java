package com.kryeit.client;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ClientsideMissionPacketUtils {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation("missions", "active_missions");
    private static List<ClientsideActiveMission> missions = List.of();

    public static List<ClientsideActiveMission> deserialize(FriendlyByteBuf buf) {
        return buf.readList(b -> new ClientsideActiveMission(buf.readInt(), buf.readItem(), buf.readComponent(), buf.readBoolean()));
    }

    public static FriendlyByteBuf serialize(List<ClientsideActiveMission> missions) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        for (ClientsideActiveMission mission : missions) {
            buf.writeInt(mission.requiredAmount());
            buf.writeItem(mission.itemStack());
            buf.writeComponent(mission.missionString());
            buf.writeBoolean(mission.isCompleted());
        }
        return buf;
    }

    public static void setMissions(List<ClientsideActiveMission> newMissions) {
        missions = newMissions;
    }

    public static List<ClientsideActiveMission> getMissions() {
        return missions;
    }
}
