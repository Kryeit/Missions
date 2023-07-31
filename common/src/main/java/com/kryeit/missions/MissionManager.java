package com.kryeit.missions;

import com.kryeit.Main;
import com.kryeit.MinecraftServerSupplier;
import com.kryeit.utils.Utils;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.toasts.MissionCompletedToast;
import com.kryeit.missions.config.ConfigReader;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MissionManager {
    public static final ResourceLocation REWARD_SOUND = new ResourceLocation("entity.player.levelup");

    public static int checkReward(MissionType type, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), item, player);
        if (activeMission == null) return 0;

        int itemsLeft = activeMission.requiredAmount() - type.getProgress(player, activeMission.item());
        if (itemsLeft <= 0) {
            ConfigReader.Mission mission = Main.getConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String rewardItem = mission.rewardItem();

            type.reset(player);
            DataStorage.INSTANCE.addReward(player, rewardItem, rewardAmount);
            DataStorage.INSTANCE.setCompleted(player, item, type.id());

            broadcastMissionCompletion(player, activeMission, type);
        }
        return itemsLeft;
    }

    public static void giveReward(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Map<String, Integer> rewards = DataStorage.INSTANCE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            ItemStack itemStack = Utils.getItem(new ResourceLocation(entry.getKey()));
            itemStack.setCount(entry.getValue());
            Utils.giveItem(itemStack, player);
        }
        DataStorage.INSTANCE.claimRewards(uuid);

        if (!rewards.isEmpty()) {
            player.connection.send(new ClientboundCustomSoundPacket(REWARD_SOUND, SoundSource.MASTER, player.position(), 1, 1));
        }
    }

    public static boolean reassignMissionsIfNecessary(UUID player) {
        int lastSunday = Utils.getDay() - Utils.getDayOfWeek();
        int assignedDay = DataStorage.INSTANCE.getLastAssignedDay(player);
        if (assignedDay < lastSunday) {
            reassignMissions(player);
            return true;
        }
        return false;
    }

    public static void reassignMissions(UUID player) {
        for (DataStorage.ActiveMission mission : getActiveMissions(player)) {
            MissionTypeRegistry.INSTANCE.getType(mission.missionID()).reset(player);
        }

        DataStorage.INSTANCE.reassignActiveMissions(Main.getConfig().getMissions(), player);
        DataStorage.INSTANCE.setLastAssignedDay(player);
    }

    public static List<DataStorage.ActiveMission> getActiveMissions(UUID playerId) {
        return DataStorage.INSTANCE.getActiveMissions(playerId);
    }

    public static boolean countItem(String missionTypeID, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(missionTypeID, item, player);
        return activeMission != null;
    }

    public static DataStorage.ActiveMission getActiveMission(String id, ResourceLocation item, UUID player) {
        List<DataStorage.ActiveMission> missions = DataStorage.INSTANCE.getActiveMissions(player);
        for (DataStorage.ActiveMission mission : missions) {
            if (mission.missionID().equals(id) && mission.item().equals(item) && !mission.isCompleted()) {
                return mission;
            }
        }
        return null;
    }

    public static void broadcastMissionCompletion(UUID player, DataStorage.ActiveMission mission, MissionType type) {
        MissionCompletedToast.send(mission.toClientMission(player));

        if (type.difficulty() == MissionDifficulty.HARD) {
            PlayerList playerList = MinecraftServerSupplier.getServer().getPlayerList();
            ServerPlayer serverPlayer = playerList.getPlayer(player);
            if (serverPlayer == null) return;

            TranslatableComponent message = new TranslatableComponent("message.hard_mission_completed", serverPlayer.getName());
            playerList.broadcastMessage(message, ChatType.CHAT, new UUID(0, 0));
        }
    }

    public static void sendMissions(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        boolean hasUnclaimedRewards = !DataStorage.INSTANCE.getUnclaimedRewards(playerUUID).isEmpty();
        List<ClientsideActiveMission> clientMissions = Utils.map(getActiveMissions(playerUUID), mission -> mission.toClientMission(playerUUID));

        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(
                ClientsideMissionPacketUtils.IDENTIFIER,
                ClientsideMissionPacketUtils.serialize(new ClientMissionData(hasUnclaimedRewards, clientMissions))
        );
        player.connection.send(packet);
    }
}
