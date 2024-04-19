package com.kryeit.missions;

import com.kryeit.MinecraftServerSupplier;
import com.kryeit.Missions;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.coins.Coins;
import com.kryeit.compat.CompatAddon;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModSounds;
import com.kryeit.registry.ModStats;
import com.kryeit.utils.Utils;
import com.simibubi.create.foundation.utility.Components;
import io.netty.buffer.Unpooled;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.kryeit.missions.config.ConfigReader.*;

public class MissionManager {
    private static final DataStorage STORAGE = new DataStorage();

    public static DataStorage getStorage() {
        return STORAGE;
    }

    public static int checkReward(MissionType type, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(type.id(), item, player);
        if (activeMission == null) return 0;

        int itemsLeft = activeMission.requiredAmount() - type.getProgress(player, activeMission.item());
        if (itemsLeft <= 0) {
            ConfigReader.Mission mission = Missions.getConfig().getMissions().get(type);
            int rewardAmount = mission.rewardAmount().getRandomValue();
            String rewardItem = mission.rewardItem();

            type.reset(player, item);
            STORAGE.addReward(player, rewardItem, rewardAmount);
            STORAGE.setCompleted(player, item, type.id());

            onMissionComplete(player, activeMission, type);
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
        Map<String, Integer> rewards = STORAGE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            ItemStack itemStack = Utils.getItem(new ResourceLocation(entry.getKey()));
            itemStack.setCount(entry.getValue());
            MinecraftServerSupplier.getServer().execute(() -> Utils.giveItem(itemStack, player));
            player.sendSystemMessage(Components.translatable("missions.menu.main.reward",
                            itemStack.getCount(),
                            Utils.removeBrackets(itemStack.getDisplayName().getString()))
                            .withStyle(ChatFormatting.GREEN)
            );
        }
        STORAGE.claimRewards(uuid);

        if (!rewards.isEmpty()) {
            player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvents.PLAYER_LEVELUP), SoundSource.MASTER, player.position().x, player.position().y, player.position().z, 1, 1, 1));
        }
    }

    public static boolean reassignMissionsIfNecessary(UUID player) {
        int lastSunday = Utils.getDay() - Utils.getDayOfWeek();
        int assignedDay = STORAGE.getLastAssignedDay(player);
        if (assignedDay < lastSunday) {
            reassignMissions(player);
            return true;
        }
        return false;
    }

    public static void reassignMissions(UUID player) {
        for (DataStorage.ActiveMission mission : getActiveMissions(player)) {
            MissionTypeRegistry.INSTANCE.getType(mission.missionID()).reset(player, mission.item());
        }

        STORAGE.reassignActiveMissions(Missions.getConfig().getMissions(), player);
        STORAGE.setLastAssignedDay(player);
        STORAGE.resetReassignments(player);
    }

    public static ReassignmentPrice calculatePrice(UUID player) {
        int freeRerolls = getTotalFreeRerolls(player);
        int rerolls = STORAGE.getReassignmentsSinceLastReset(player);

        if (freeRerolls > rerolls) {
            return new ReassignmentPrice(Coins.getCoin(0).getItem(), 1);
        }

        int price = 2 << rerolls - freeRerolls;
        int coinIndex = (int) Utils.log(64, price - 1);

        int coinAmount = (int) (price / Math.pow(64, coinIndex));

        return new ReassignmentPrice(Coins.getCoin(coinIndex + FIRST_REROLL_CURRENCY).getItem(), coinAmount);
    }

    public static void tryReassignMission(ServerPlayer serverPlayer, int index) {
        UUID player = serverPlayer.getUUID();
        DataStorage.ActiveMission activeMission = getActiveMissions(player).get(index);
        if (activeMission.isCompleted()) return;

        ReassignmentPrice price = calculatePrice(player);

        if (price.amount == 1 || Utils.removeItems(serverPlayer.getInventory(), price.item, price.amount)) {
            STORAGE.reassignActiveMission(Missions.getConfig().getMissions(), player, index);
            MissionTypeRegistry.INSTANCE.getType(activeMission.missionID()).reset(player, activeMission.item());
            STORAGE.incrementReassignmentsSinceLastReset(player);
            serverPlayer.awardStat(ModStats.MISSIONS_REROLLED);
        }
    }

    public static List<DataStorage.ActiveMission> getActiveMissions(UUID playerId) {
        return STORAGE.getActiveMissions(playerId);
    }

    public static boolean countItem(String missionTypeID, UUID player, ResourceLocation item) {
        DataStorage.ActiveMission activeMission = getActiveMission(missionTypeID, item, player);
        return activeMission != null;
    }

    public static DataStorage.ActiveMission getActiveMission(String id, ResourceLocation item, UUID player) {
        List<DataStorage.ActiveMission> missions = STORAGE.getActiveMissions(player);
        for (DataStorage.ActiveMission mission : missions) {
            if (mission.missionID().equals(id) && mission.item().equals(item) && !mission.isCompleted()) {
                return mission;
            }
        }
        return null;
    }

    public static void onMissionComplete(UUID player, DataStorage.ActiveMission mission, MissionType type) {
        PlayerList playerList = MinecraftServerSupplier.getServer().getPlayerList();
        ServerPlayer serverPlayer = playerList.getPlayer(player);
        MissionDifficulty difficulty = MissionTypeRegistry.INSTANCE.getType(mission.missionID()).difficulty();

        if (serverPlayer == null) return;

        switch (difficulty) {
            case EASY -> serverPlayer.awardStat(ModStats.EASY_MISSIONS_COMPLETED);
            case NORMAL -> serverPlayer.awardStat(ModStats.NORMAL_MISSIONS_COMPLETED);
            case HARD -> serverPlayer.awardStat(ModStats.HARD_MISSIONS_COMPLETED);
        }

        serverPlayer.connection.send(new ClientboundSoundPacket(Holder.direct(ModSounds.MISSION_COMPLETE.get()), SoundSource.NEUTRAL, serverPlayer.position().x, serverPlayer.position().y, serverPlayer.position().z, 1, 1, 1));
        Utils.executeCommandAsServer(COMMAND_UPON_MISSION.replace("%player%", serverPlayer.getName().getString()));

        showToast(serverPlayer, mission.toClientMission(player));

        if (type.difficulty() == MissionDifficulty.HARD) {
            Component message = Components.translatable("missions.message.hard_mission_completed", serverPlayer.getName())
                    .withStyle(ChatFormatting.GOLD);
            playerList.broadcastSystemMessage(message, false);

            if (Math.random() <= EXCHANGER_DROP_RATE) {
                MinecraftServerSupplier.getServer().execute(() -> Utils.giveItem(ModBlocks.MECHANICAL_EXCHANGER.asStack(), serverPlayer));
            }
        }
    }

    private static void showToast(ServerPlayer player, ClientsideActiveMission mission) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        mission.toBuffer(buffer);
        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(
                ClientsideMissionPacketUtils.SHOW_TOAST,
                buffer
        );
        player.connection.send(packet);
    }

    public static void sendMissions(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        boolean hasUnclaimedRewards = !STORAGE.getUnclaimedRewards(playerUUID).isEmpty();
        List<ClientsideActiveMission> clientMissions = Utils.map(getActiveMissions(playerUUID), mission -> mission.toClientMission(playerUUID));

        ReassignmentPrice price = MissionManager.calculatePrice(playerUUID);
        boolean canReroll = player.getInventory().countItem(price.item()) >= price.amount();

        int rerolls = STORAGE.getReassignmentsSinceLastReset(playerUUID);
        int freeRerollsLeft = Math.max(0, getTotalFreeRerolls(playerUUID) - rerolls);

        ClientMissionData data = new ClientMissionData(hasUnclaimedRewards, clientMissions, price.asStack(), freeRerollsLeft, freeRerollsLeft > 0 || canReroll);

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

    @SuppressWarnings("UnreachableCode")
    private static int getTotalFreeRerolls(UUID player) {
        int defaultValue = FREE_REROLLS;

        if (!CompatAddon.LUCKPERMS.isLoaded())
            return defaultValue;

        LuckPerms luckPerms = LuckPermsProvider.get();

        User user = luckPerms.getUserManager().getUser(player);
        if (user == null) return defaultValue;

        QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(user).orElse(null);
        if (queryOptions == null) return defaultValue;

        String contextKey = "amount";
        for (PermissionNode node : user.resolveInheritedNodes(NodeType.PERMISSION, queryOptions)) {
            if (node.getKey().equals("missions.freerolls")) {
                Integer amount = node.getContexts().getAnyValue(contextKey).map(Integer::valueOf).orElse(null);
                if (amount != null) {
                    return amount;
                }
            }
        }

        return defaultValue;
    }
}
