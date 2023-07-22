package com.kryeit.client;

import com.kryeit.missions.MissionDifficulty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record ClientsideActiveMission(Component titleString, MissionDifficulty difficulty, int requiredAmount, int progress, ItemStack itemStack, Component missionString, boolean isCompleted) {

    public static ClientsideActiveMission fromBuffer(FriendlyByteBuf buf) {
        return new ClientsideActiveMission(buf.readComponent(), buf.readEnum(MissionDifficulty.class), buf.readInt(), buf.readInt(), buf.readItem(), buf.readComponent(), buf.readBoolean());
    }

    public String item() {
        return itemStack.getDescriptionId();
    }
}
