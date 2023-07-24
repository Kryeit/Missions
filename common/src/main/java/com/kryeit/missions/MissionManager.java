package com.kryeit.missions;

import com.kryeit.Main;
import com.kryeit.Utils;
import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.missions.config.ConfigReader;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MissionManager {

    public static final ResourceLocation REWARD_SOUND = new ResourceLocation("entity.player.levelup");

    public static void checkReward(MissionType type, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), item, player);
        if (activeMission == null) return; // TODO maybe throw an exception

        if (type.getProgress(player, activeMission.item()) >= activeMission.requiredAmount()) {
            ConfigReader.Mission mission = Main.getConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String rewardItem = mission.rewardItem();

            type.reset(player);
            DataStorage.INSTANCE.addReward(player, rewardItem, rewardAmount);
            DataStorage.INSTANCE.setCompleted(player, item, type.id());
        }
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

    public static void sendMissions(ServerPlayer player) {
        List<ClientsideActiveMission> clientMissions = new ArrayList<>();
        for (DataStorage.ActiveMission mission : getActiveMissions(player.getUUID())) {
            clientMissions.add(mission.toClientMission(player.getUUID()));
        }

        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(
                ClientsideMissionPacketUtils.IDENTIFIER,
                ClientsideMissionPacketUtils.serialize(clientMissions)
        );
        player.connection.send(packet);
    }
}
