package com.kryeit.missions;

import net.minecraft.world.item.ItemStack;

public class ActiveMission {
    private final int requiredAmount;
    private final ItemStack itemStack;
    private boolean isCompleted = true;

    public ActiveMission(int requiredAmount, ItemStack itemStack) {
        this.requiredAmount = requiredAmount;
        this.itemStack = itemStack();
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public int requiredAmount() {
        return requiredAmount;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
