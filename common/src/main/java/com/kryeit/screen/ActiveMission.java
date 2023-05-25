package com.kryeit.screen;

import net.minecraft.world.item.ItemStack;

public class ActiveMission {
    private final int requiredAmount;
    private final ItemStack itemStack;
    private final String missionString;
    private boolean isCompleted = true;

    public ActiveMission(int requiredAmount, ItemStack itemStack, String missionString) {
        this.requiredAmount = requiredAmount;
        this.itemStack = itemStack;
        this.missionString = missionString;
    }

    public String item() {
        return itemStack.getDescriptionId();
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public String missionString() {
        return missionString;
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
