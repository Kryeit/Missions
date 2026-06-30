package com.kryeit.missions;

import com.kryeit.MinecraftServerSupplier;
import com.kryeit.Missions;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.coins.Coins;
import com.kryeit.compat.CompatAddon;
import com.kryeit.network.MissionPayloads;
import com.kryeit.utils.Lang;
import com.kryeit.utils.Utils;
import net.neoforged.neoforge.network.PacketDistributor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import java.util.Random;
import java.util.UUID;

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
            type.reset(player, item);
            // Store the reward as a range; the actual amount is rolled when the player claims it.
            STORAGE.addReward(player, activeMission.rewardItem(), activeMission.rewardMin(), activeMission.rewardMax());
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

    private static final Random RANDOM = new Random();

    public static void giveReward(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Map<String, int[]> rewards = STORAGE.getUnclaimedRewards(uuid);
        for (Map.Entry<String, int[]> entry : rewards.entrySet()) {
            int[] range = entry.getValue();
            int rolled = range[0] >= range[1] ? range[0] : RANDOM.nextInt(range[0], range[1] + 1);
            if (rolled <= 0) continue;
            ItemStack itemStack = Utils.getItem(ResourceLocation.parse(entry.getKey()));
            itemStack.setCount(rolled);
            MinecraftServerSupplier.getServer().execute(() -> Utils.giveItem(itemStack, player));
            player.sendSystemMessage(Lang.t("missions.menu.main.reward", "Received %s %s",
                    rolled, itemStack.getHoverName()).withStyle(ChatFormatting.GREEN));
        }
        STORAGE.claimRewards(uuid);

        if (!rewards.isEmpty()) {
            player.connection.send(new ClientboundSoundPacket(Holder.direct(SoundEvents.PLAYER_LEVELUP), SoundSource.MASTER, player.position().x, player.position().y, player.position().z, 1, 1, 1));
        }
    }

    public static boolean reassignMissionsIfNecessary(UUID player) {
        int lastAssignedDay = STORAGE.getLastAssignedDay(player);

        boolean reassign = switch (Missions.getConfig().reassignInterval) {
            case DAILY -> lastAssignedDay != Utils.getDay();
            case WEEKLY -> lastAssignedDay < Utils.getDay() - Utils.getDayOfWeek();
        };
        if (reassign) {
            reassignMissions(player);
        }
        return reassign;
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

        int price = 2 << (rerolls - freeRerolls);
        int coinIndex = (int) Utils.log(64, price - 1);

        int coinAmount = (int) (price / Math.pow(64, coinIndex));

        return new ReassignmentPrice(Coins.getCoin(coinIndex + Missions.getConfig().firstRerollCurrency).getItem(), coinAmount);
    }

    public static void tryReassignMission(ServerPlayer serverPlayer, int index) {
        UUID player = serverPlayer.getUUID();
        List<DataStorage.ActiveMission> missions = getActiveMissions(player);
        if (index < 0 || index >= missions.size()) return;
        DataStorage.ActiveMission activeMission = missions.get(index);
        if (activeMission.isCompleted()) return;

        ReassignmentPrice price = calculatePrice(player);

        if (price.amount == 1 || Utils.removeItems(serverPlayer.getInventory(), price.item, price.amount)) {
            STORAGE.reassignActiveMission(Missions.getConfig().getMissions(), player, index);

            if (!Utils.contains(getActiveMissions(player), m -> m.missionID().equals(activeMission.missionID()) && m.item().equals(activeMission.item()))) {
                MissionTypeRegistry.INSTANCE.getType(activeMission.missionID()).reset(player, activeMission.item());
            }

            STORAGE.incrementReassignmentsSinceLastReset(player);
            STORAGE.incrementStat(player, "rerolled");
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

        List<DataStorage.ActiveMission> activeMissions = getActiveMissions(player);
        if (serverPlayer == null) return;

        STORAGE.incrementStat(player, difficulty.statKey());

        Utils.executeCommandAsServer(Missions.getConfig().commandUponMission.replace("%player%", serverPlayer.getName().getString()));

        // Custom client toast (only seen by clients that have the mod installed).
        showToast(serverPlayer, mission.toClientMission(player));

        // Server-built chat message (renders on vanilla clients, unlike a custom client toast).
        // Show the reward RANGE here; the exact amount is only revealed when claimed.
        ItemStack rewardStack = Utils.getItem(ResourceLocation.parse(mission.rewardItem()));
        String rewardRange = mission.rewardMin() == mission.rewardMax()
                ? String.valueOf(mission.rewardMin())
                : mission.rewardMin() + "-" + mission.rewardMax();
        serverPlayer.sendSystemMessage(Lang.t("missions.message.completed",
                "✔ Completed mission: %s (reward: %s %s, claim it!)",
                Component.literal(mission.title()),
                rewardRange,
                rewardStack.getHoverName()).withStyle(ChatFormatting.GREEN));

        if (type.difficulty() == MissionDifficulty.HARD) {
            Component message = Lang.t("missions.message.hard_mission_completed",
                    "%s has just completed a hard mission!", serverPlayer.getName()).withStyle(ChatFormatting.GOLD);
            playerList.broadcastSystemMessage(message, false);
        }

        if (activeMissions.stream().allMatch(DataStorage.ActiveMission::isCompleted)) {
            ItemStack bonus = Missions.getConfig().getAllMissionsReward();
            if (bonus != null && !bonus.isEmpty()) {
                MinecraftServerSupplier.getServer().execute(() -> Utils.giveItem(bonus.copy(), serverPlayer));
                serverPlayer.sendSystemMessage(Lang.t("missions.message.all_complete",
                        "All missions complete! Bonus reward granted!").withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
    }

    /** Pushes the player's full mission snapshot to their client so the custom screen can render it. */
    public static void sendMissions(ServerPlayer player) {
        UUID uuid = player.getUUID();
        boolean hasUnclaimedRewards = !STORAGE.getUnclaimedRewards(uuid).isEmpty();
        List<ClientsideActiveMission> clientMissions = Utils.map(getActiveMissions(uuid), mission -> mission.toClientMission(uuid));

        ReassignmentPrice price = calculatePrice(uuid);
        boolean canReroll = player.getInventory().countItem(price.item()) >= price.amount();

        int rerolls = STORAGE.getReassignmentsSinceLastReset(uuid);
        int freeRerollsLeft = Math.max(0, getTotalFreeRerolls(uuid) - rerolls);

        ClientMissionData data = new ClientMissionData(
                hasUnclaimedRewards,
                clientMissions,
                price.asStack(),
                freeRerollsLeft,
                freeRerollsLeft > 0 || canReroll,
                STORAGE.getStat(uuid, MissionDifficulty.EASY.statKey()),
                STORAGE.getStat(uuid, MissionDifficulty.NORMAL.statKey()),
                STORAGE.getStat(uuid, MissionDifficulty.HARD.statKey()),
                STORAGE.getStat(uuid, "rerolled"));

        PacketDistributor.sendToPlayer(player, new MissionPayloads.ActiveMissions(data));
    }

    private static void showToast(ServerPlayer player, ClientsideActiveMission mission) {
        PacketDistributor.sendToPlayer(player, new MissionPayloads.ShowToast(mission));
    }

    public record ReassignmentPrice(Item item, int amount) {
        public ItemStack asStack() {
            ItemStack stack = item.getDefaultInstance();
            stack.setCount(amount);
            return stack;
        }
    }

    public static int getTotalFreeRerolls(UUID player) {
        int defaultValue = Missions.getConfig().freeRerolls;

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
