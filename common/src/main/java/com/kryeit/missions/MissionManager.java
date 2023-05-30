package com.kryeit.missions;

import com.kryeit.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MissionManager {
    public static void checkReward(MissionType type, UUID player) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), player);
        if (activeMission == null || activeMission.isCompleted()) return;

        if (type.getProgress(player, activeMission.item()) >= activeMission.requiredAmount()) {
            ConfigReader.Mission mission = gibConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String item = mission.rewardItem();

            type.reset(player);
            DataStorage.INSTANCE.addReward(player, item, rewardAmount);
        }
    }

    public static void giveReward(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Map<String, Integer> rewards = DataStorage.INSTANCE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            ItemStack itemStack = Registry.ITEM.get(new ResourceLocation(entry.getKey())).getDefaultInstance();
            itemStack.setCount(entry.getValue());
            giveItem(itemStack, player);
        }
        DataStorage.INSTANCE.claimRewards(uuid);
    }

    public static boolean reassignMissionsIfNecessary(UUID player) {
        int lastSunday = Utils.getDay() - Utils.getDayOfWeek();
        int assignedDay = DataStorage.INSTANCE.getLastAssignedDay(player);
        if (assignedDay < lastSunday) {
            DataStorage.INSTANCE.reassignActiveMissions(gibConfig().getMissions(), player);
            DataStorage.INSTANCE.setLastAssignedDay(player);
            return true;
        }
        return false;
    }

    private static void giveItem(ItemStack stack, ServerPlayer player) {
        int stackSize = stack.getMaxStackSize();
        int l = stack.getCount();
        while (l > 0) {
            ItemEntity itemEntity;
            int m = Math.min(stackSize, l);
            l -= m;
            ItemStack itemStack = stack.copy();
            itemStack.setCount(m);

            boolean added = player.getInventory().add(itemStack);
            if (!added || !itemStack.isEmpty()) {
                itemEntity = player.drop(itemStack, false);
                if (itemEntity == null) continue;
                itemEntity.setNoPickUpDelay();
                itemEntity.setOwner(player.getUUID());
                continue;
            }
            itemStack.setCount(1);
            itemEntity = player.drop(itemStack, false);
            if (itemEntity != null) {
                itemEntity.makeFakeItem();
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            player.containerMenu.broadcastChanges();
        }
    }

    public static List<DataStorage.ActiveMission> getActiveMissions(UUID playerId) {
        return DataStorage.INSTANCE.getActiveMissions(playerId);
    }

    public static DataStorage.ActiveMission getActiveMission(String id, UUID player) {
        List<DataStorage.ActiveMission> missions = DataStorage.INSTANCE.getActiveMissions(player);
        for (DataStorage.ActiveMission mission : missions) {
            if (mission.missionID().equals(id)) {
                return mission;
            }
        }
        return null;
    }

    public static ConfigReader gibConfig() {
        try {
            return ConfigReader.readFile(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
