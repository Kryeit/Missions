package com.kryeit.client;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ClientMissionData(boolean hasUnclaimedRewards, List<ClientsideActiveMission> activeMissions,
                                ItemStack rerollPrice, int freeRerollsLeft, boolean canReroll) {

    public record ClientsideActiveMission(Component titleString, MissionDifficulty difficulty, int requiredAmount,
                                          String missionType, int progress, ItemStack previewItem, ItemStack itemRequired,
                                          Component missionString, boolean isCompleted, int rewardAmount,
                                          String rewardItemLocation) {

        public static ClientsideActiveMission fromBuffer(FriendlyByteBuf buf) {
            return new ClientsideActiveMission(buf.readComponent(),
                    buf.readEnum(MissionDifficulty.class),
                    buf.readInt(),
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readItem(),
                    buf.readItem(),
                    buf.readComponent(),
                    buf.readBoolean(),
                    buf.readInt(),
                    buf.readUtf());
        }

        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeComponent(titleString());
            buf.writeEnum(difficulty());
            buf.writeInt(requiredAmount());
            buf.writeUtf(missionType());
            buf.writeInt(progress());
            buf.writeItem(previewItem());
            buf.writeItem(itemRequired());
            buf.writeComponent(missionString());
            buf.writeBoolean(isCompleted());
            buf.writeInt(rewardAmount());
            buf.writeUtf(rewardItemLocation());
        }
    }
}
