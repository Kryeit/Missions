package com.kryeit.client;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Snapshot of a player's mission state sent to the client to render the custom Missions screen.
 * This is a plain data record with no client-only type references, so it is safe to build on the
 * dedicated server. Serialization uses NeoForge/Mojang StreamCodecs (the old FriendlyByteBuf
 * readItem/writeItem/readComponent helpers no longer exist in 1.21.1).
 */
public record ClientMissionData(boolean hasUnclaimedRewards, List<ClientsideActiveMission> activeMissions,
                                ItemStack rerollPrice, int freeRerollsLeft, boolean canReroll,
                                int easyCompleted, int normalCompleted, int hardCompleted, int missionsRerolled) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientMissionData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeBoolean(data.hasUnclaimedRewards());
                buf.writeVarInt(data.activeMissions().size());
                for (ClientsideActiveMission mission : data.activeMissions()) {
                    ClientsideActiveMission.STREAM_CODEC.encode(buf, mission);
                }
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, data.rerollPrice());
                buf.writeVarInt(data.freeRerollsLeft());
                buf.writeBoolean(data.canReroll());
                buf.writeVarInt(data.easyCompleted());
                buf.writeVarInt(data.normalCompleted());
                buf.writeVarInt(data.hardCompleted());
                buf.writeVarInt(data.missionsRerolled());
            },
            buf -> {
                boolean hasUnclaimedRewards = buf.readBoolean();
                int count = buf.readVarInt();
                List<ClientsideActiveMission> missions = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    missions.add(ClientsideActiveMission.STREAM_CODEC.decode(buf));
                }
                ItemStack rerollPrice = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                int freeRerollsLeft = buf.readVarInt();
                boolean canReroll = buf.readBoolean();
                int easy = buf.readVarInt();
                int normal = buf.readVarInt();
                int hard = buf.readVarInt();
                int rerolled = buf.readVarInt();
                return new ClientMissionData(hasUnclaimedRewards, missions, rerollPrice, freeRerollsLeft,
                        canReroll, easy, normal, hard, rerolled);
            });

    public record ClientsideActiveMission(Component titleString, MissionDifficulty difficulty, int requiredAmount,
                                          String missionType, int progress, ItemStack previewItem, ItemStack itemRequired,
                                          Component missionString, boolean isCompleted, int rewardMin, int rewardMax,
                                          String rewardItemLocation) {

        public static final StreamCodec<RegistryFriendlyByteBuf, ClientsideActiveMission> STREAM_CODEC = StreamCodec.of(
                (buf, mission) -> {
                    ComponentSerialization.STREAM_CODEC.encode(buf, mission.titleString());
                    buf.writeEnum(mission.difficulty());
                    buf.writeVarInt(mission.requiredAmount());
                    buf.writeUtf(mission.missionType());
                    buf.writeVarInt(mission.progress());
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, mission.previewItem());
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, mission.itemRequired());
                    ComponentSerialization.STREAM_CODEC.encode(buf, mission.missionString());
                    buf.writeBoolean(mission.isCompleted());
                    buf.writeVarInt(mission.rewardMin());
                    buf.writeVarInt(mission.rewardMax());
                    buf.writeUtf(mission.rewardItemLocation());
                },
                buf -> new ClientsideActiveMission(
                        ComponentSerialization.STREAM_CODEC.decode(buf),
                        buf.readEnum(MissionDifficulty.class),
                        buf.readVarInt(),
                        buf.readUtf(),
                        buf.readVarInt(),
                        ItemStack.OPTIONAL_STREAM_CODEC.decode(buf),
                        ItemStack.OPTIONAL_STREAM_CODEC.decode(buf),
                        ComponentSerialization.STREAM_CODEC.decode(buf),
                        buf.readBoolean(),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readUtf()));

        /** Single-value reward shown in tooltips/toasts; a range collapses to "min-max". */
        public String rewardText() {
            return rewardMin == rewardMax ? String.valueOf(rewardMin) : rewardMin + "-" + rewardMax;
        }
    }
}
