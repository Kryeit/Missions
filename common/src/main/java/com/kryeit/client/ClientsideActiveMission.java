package com.kryeit.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record ClientsideActiveMission(int requiredAmount, ItemStack itemStack, Component missionString,
                                      boolean isCompleted) {

    public String item() {
        return itemStack.getDescriptionId();
    }
}
