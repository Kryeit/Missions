package com.kryeit.missions;

import com.kryeit.Main;
import com.kryeit.MinecraftServerSupplier;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.toasts.MissionCompletedToast;
import com.kryeit.coins.Coins;
import com.kryeit.entry.ModBlocks;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
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

    public static void incrementMission(UUID player, String missionID, ResourceLocation key, int amount) {
        MissionType type = MissionTypeRegistry.INSTANCE.getType(missionID);
        incrementMission(player, type, key, amount);
    }

    public static void incrementMission(UUID player, Class<? extends MissionType> clazz, ResourceLocation key, int amount) {
        MissionType type = MissionTypeRegistry.INSTANCE.getType(clazz);
        incrementMission(player, type, key, amount);
    }

    public static void incrementMission(UUID player, MissionType type, ResourceLocation key, int amount) {
        if (MissionManager.countItem(type.id(), player, key)) {
            CompoundTag data = type.getData(player);
            type.increment(amount, key, data);

            int itemsLeft = MissionManager.checkReward(type, player, key);
            // positive when not enough items, negative when too many items -> recurse when negative
            if (itemsLeft < 0) {
                incrementMission(player, type, key, -itemsLeft);
            }
        }
    }

    public static void giveReward(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Map<String, Integer> rewards = DataStorage.INSTANCE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            ItemStack itemStack = Utils.getItem(new ResourceLocation(entry.getKey()));
            itemStack.setCount(entry.getValue());
            Utils.giveItem(itemStack, player);
            player.sendMessage(new TranslatableComponent("missions.menu.main.reward",
                    itemStack.getCount(),
                    Utils.removeBrackets(itemStack.getDisplayName().getString()))
                    .withStyle(ChatFormatting.GREEN),
                    player.getUUID());
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
        DataStorage.INSTANCE.resetReassignments(player);
    }

    public static ReassignmentPrice calculatePrice(UUID player) {
        int price = 2 << DataStorage.INSTANCE.getReassignmentsSinceLastReset(player);
        int coinIndex = (int) Utils.log(64, price - 1);
        int coinAmount = (int) (price / Math.pow(64, coinIndex));

        return new ReassignmentPrice(Coins.getCoin(coinIndex).getItem(), coinAmount);
    }

    public static void tryReassignMission(ServerPlayer serverPlayer, int index) {
        UUID player = serverPlayer.getUUID();
        DataStorage.ActiveMission activeMission = getActiveMissions(player).get(index);
        if (activeMission.isCompleted()) return;

        ReassignmentPrice price = calculatePrice(player);
        if (Utils.removeItems(serverPlayer.getInventory(), price.item(), price.amount())) {
            DataStorage.INSTANCE.reassignActiveMission(Main.getConfig().getMissions(), player, index);
            MissionTypeRegistry.INSTANCE.getType(activeMission.missionID()).reset(player);
            DataStorage.INSTANCE.incrementReassignmentsSinceLastReset(player);
        }
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

            MutableComponent message = new TranslatableComponent("missions.message.hard_mission_completed", serverPlayer.getName())
                    .withStyle(ChatFormatting.GOLD);
            playerList.broadcastMessage(message, ChatType.CHAT, new UUID(0, 0));

            if (Math.random() < 0.01) {
                Utils.giveItem(ModBlocks.EXCHANGE_ATM.asStack(), serverPlayer);
            }
        }
    }

    public static void sendMissions(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        boolean hasUnclaimedRewards = !DataStorage.INSTANCE.getUnclaimedRewards(playerUUID).isEmpty();
        List<ClientsideActiveMission> clientMissions = Utils.map(getActiveMissions(playerUUID), mission -> mission.toClientMission(playerUUID));

        ReassignmentPrice price = MissionManager.calculatePrice(playerUUID);
        boolean canReroll = player.getInventory().countItem(price.item()) >= price.amount();

        ClientMissionData data = new ClientMissionData(hasUnclaimedRewards, clientMissions, price.asStack(), 42, canReroll);

        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(
                ClientsideMissionPacketUtils.IDENTIFIER,
                ClientsideMissionPacketUtils.serialize(data)
        );
        player.connection.send(packet);
    }

    public record ReassignmentPrice(Item item, int amount) {
        public ItemStack asStack() {
            ItemStack stack = item.getDefaultInstance();
            stack.setCount(amount);
            return stack;
        }
    }
}
