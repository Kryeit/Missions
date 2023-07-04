package com.kryeit.missions;

import com.kryeit.Main;
import com.kryeit.Utils;
import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MissionManager {
    public static void checkReward(MissionType type, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), item, player);
        if (activeMission == null) return; // TODO maybe throw an exceptio

        if (type.getProgress(player, activeMission.item()) >= activeMission.requiredAmount()) {
            ConfigReader.Mission mission = Main.getConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String rewardItem = mission.rewardItem();

            type.reset(player);
            DataStorage.INSTANCE.addReward(player, rewardItem, rewardAmount);
            DataStorage.INSTANCE.setCompleted(player, item, type.id(), true);
        }
    }

    public static void giveReward(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Map<String, Integer> rewards = DataStorage.INSTANCE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            ItemStack itemStack = Utils.getItem(new ResourceLocation(entry.getKey()));
            itemStack.setCount(entry.getValue());
            giveItem(itemStack, player);
        }
        DataStorage.INSTANCE.claimRewards(uuid);
    }

    public static boolean reassignMissionsIfNecessary(UUID player) {
        int lastSunday = Utils.getDay() - Utils.getDayOfWeek();
        int assignedDay = DataStorage.INSTANCE.getLastAssignedDay(player);
        if (assignedDay < lastSunday) {
            DataStorage.INSTANCE.reassignActiveMissions(Main.getConfig().getMissions(), player);
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

    public static boolean countItem(String missionTypeID, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(missionTypeID, item, player);
        return activeMission != null && !activeMission.isCompleted();
    }

    public static DataStorage.ActiveMission getActiveMission(String id, ResourceLocation item, UUID player) {
        List<DataStorage.ActiveMission> missions = DataStorage.INSTANCE.getActiveMissions(player);
        for (DataStorage.ActiveMission mission : missions) {
            if (mission.missionID().equals(id) && mission.item().equals(item)) {
                return mission;
            }
        }
        return null;
    }

    public static void updateMissions(ServerPlayer player) {
        List<ClientsideActiveMission> clientMissions = new ArrayList<>();
        for (DataStorage.ActiveMission mission : getActiveMissions(player.getUUID())) {
            clientMissions.add(mission.toClientMission("", player.getUUID()));
        }

        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(
                ClientsideMissionPacketUtils.IDENTIFIER,
                ClientsideMissionPacketUtils.serialize(clientMissions)
        );
        player.connection.send(packet);
    }
}
