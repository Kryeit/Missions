package com.kryeit.queue;

import com.kryeit.Main;
import com.kryeit.MinecraftServerSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//public class Queue {
//    public static final float PRIORITY_SLOT_PERCENTAGE = 0.3F;
//
//    public List<UUID> queue = new ArrayList<>();
//    public List<UUID> priorityQueue = new ArrayList<>();
//
//    public Queue() {
//    }
//
//    public int getSize(boolean isPriority) {
//        return isPriority ? priorityQueue.size() : queue.size();
//    }
//
//    public UUID getPos(int i, boolean isPriority) {
//        return isPriority ? priorityQueue.get(i) : queue.get(i);
//    }
//
//    public int getPos(UUID player) {
//        int pos = 1;
//        for (UUID id : isPriority(player) ? priorityQueue : queue) {
//            if (id.equals(player))
//                return pos;
//            pos++;
//        }
//        return -1;
//    }
//
//    public boolean isPlayerQueued(UUID player) {
//        return priorityQueue.contains(player) || queue.contains(player);
//    }
//    public boolean add(UUID player) {
//        if (isPlayerQueued(player))
//            return false;
//
//        return isPriority(player) ? priorityQueue.add(player) : queue.add(player);
//    }
//
//    public boolean remove(UUID player) {
//        if (!isPlayerQueued(player))
//            return false;
//
//        return priorityQueue.remove(player) || queue.remove(player);
//    }
//
//    public boolean canJoin(UUID player) {
//        if (isPriority(player)) {
//            return getMaxPriorityOnline() > getPriorityOnline() && getPos(player) == 1;
//        } else {
//            return getMaxOnline() - getMaxPriorityOnline() > getOnline() - getPriorityOnline() && getPos(player) == 1;
//        }
//    }
//
//    public static int getPriorityOnline() {
//        int count = 0;
//        for (ServerPlayer player : MinecraftServerSupplier.getServer().getPlayerList().getPlayers()) {
//            if (isPriority(player.getUUID()))
//                count ++;
//        }
//        return count;
//    }
//
//    public static int getOnline() {
//        return MinecraftServerSupplier.getServer().getPlayerList().getPlayerCount();
//    }
//
//    public static int getMaxPriorityOnline() {
//        return (int) (getMaxOnline()/PRIORITY_SLOT_PERCENTAGE);
//    }
//
//    public static int getMaxOnline() {
//        return MinecraftServerSupplier.getServer().getPlayerList().getMaxPlayers();
//    }
//
//    public static boolean isPriority(UUID player) {
//        return true;
//    }
//}
//