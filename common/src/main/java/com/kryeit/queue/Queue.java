package com.kryeit.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Queue {

    public List<UUID> queue = new ArrayList<>();
    public List<UUID> priorityQueue = new ArrayList<>();

    public Queue() {
    }

    public int getSize(boolean isPriority) {
        return isPriority ? priorityQueue.size() : queue.size();
    }

    public UUID getPos(int i, boolean isPriority) {
        return isPriority ? priorityQueue.get(i) : queue.get(i);
    }

    public boolean isPriority(UUID player) {
        return true;
    }

    public boolean isPlayerQueued(UUID player) {
        return priorityQueue.contains(player) || queue.contains(player);
    }
    public boolean add(UUID player) {
        if (isPlayerQueued(player))
            return false;

        return isPriority(player) ? priorityQueue.add(player) : queue.add(player);
    }

    public boolean remove(UUID player) {
        if (!isPlayerQueued(player))
            return false;

        return priorityQueue.remove(player) || queue.remove(player);
    }

    public boolean canJoin() {
        return true;
    }
}
