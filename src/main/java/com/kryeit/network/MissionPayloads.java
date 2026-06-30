package com.kryeit.network;

import com.kryeit.Missions;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Custom packet payloads for the Missions screen, replacing the old Architectury custom-payload
 * mixin layer. Three are client to server (request data, reroll, claim) and three are server to
 * client (open screen, push data, show toast).
 */
public final class MissionPayloads {
    private MissionPayloads() {}

    // ---- Client -> Server ----

    public record RequestMissions() implements CustomPacketPayload {
        public static final Type<RequestMissions> TYPE = new Type<>(Missions.asResource("request_missions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestMissions> CODEC = StreamCodec.unit(new RequestMissions());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record Payout() implements CustomPacketPayload {
        public static final Type<Payout> TYPE = new Type<>(Missions.asResource("payout"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Payout> CODEC = StreamCodec.unit(new Payout());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record Reroll(int index) implements CustomPacketPayload {
        public static final Type<Reroll> TYPE = new Type<>(Missions.asResource("reroll"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Reroll> CODEC =
                StreamCodec.composite(ByteBufCodecs.VAR_INT, Reroll::index, Reroll::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // ---- Server -> Client ----

    public record OpenMissions() implements CustomPacketPayload {
        public static final Type<OpenMissions> TYPE = new Type<>(Missions.asResource("open_missions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenMissions> CODEC = StreamCodec.unit(new OpenMissions());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record ActiveMissions(ClientMissionData data) implements CustomPacketPayload {
        public static final Type<ActiveMissions> TYPE = new Type<>(Missions.asResource("active_missions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ActiveMissions> CODEC =
                StreamCodec.composite(ClientMissionData.STREAM_CODEC, ActiveMissions::data, ActiveMissions::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record ShowToast(ClientsideActiveMission mission) implements CustomPacketPayload {
        public static final Type<ShowToast> TYPE = new Type<>(Missions.asResource("show_toast"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ShowToast> CODEC =
                StreamCodec.composite(ClientsideActiveMission.STREAM_CODEC, ShowToast::mission, ShowToast::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
