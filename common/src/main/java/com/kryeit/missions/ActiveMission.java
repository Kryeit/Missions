package com.kryeit.missions;

public class ActiveMission {
    private final int requiredAmount;
    private final String item;
    private boolean isCompleted = true;

    public ActiveMission(int requiredAmount, String item) {
        this.requiredAmount = requiredAmount;
        this.item = item;
    }

    public String item() {
        return item;
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
