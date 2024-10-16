package com.kryeit.utils;

import com.kryeit.MinecraftServerSupplier;
import com.kryeit.client.ClientMissionData;
import com.kryeit.missions.MissionType;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.registry.ModStats;
import com.mojang.brigadier.ParseResults;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
    private static final Random RANDOM = new Random();
    private static final ItemStack DEFAULT_SPAWN_EGG = BuiltInRegistries.ITEM.get(new ResourceLocation("player_head")).getDefaultInstance();

    public static int getDay() {
        return (int) (System.currentTimeMillis() / 86_400_000);
    }

    /**
     * @return The current day of the week. 0 is monday.
     */
    public static int getDayOfWeek() {
        return (getDay() + 3) % 7;
    }

    public static ItemStack getItem(ResourceLocation item) {
        return BuiltInRegistries.ITEM.get(item).getDefaultInstance();
    }

    public static ItemStack getItem(ResourceLocation item, int amount) {
        ItemStack stack = getItem(item);
        stack.setCount(amount);
        return stack;
    }

    @SuppressWarnings("Contract")
    @Contract("_ -> !null")
    @ExpectPlatform
    public static boolean isModLoaded(String id) {
        throw new AssertionError();
    }

    public static void broadcastMessage(Component message) {
        MinecraftServer server = MinecraftServerSupplier.getServer();
        if (server != null) {
            server.getPlayerList().broadcastSystemMessage(message, false);
        }
    }

    public static void giveItem(ItemStack stack, ServerPlayer player) {
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
                itemEntity.setTarget(player.getUUID());
                continue;
            }
            itemStack.setCount(1);
            itemEntity = player.drop(itemStack, false);
            if (itemEntity != null) {
                itemEntity.makeFakeItem();
            }
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            player.containerMenu.broadcastChanges();
        }
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> mappingFunction) {
        List<R> out = new ArrayList<>();
        for (T t : list) {
            out.add(mappingFunction.apply(t));
        }
        return out;
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> out = new ArrayList<>();
        for (T t : collection) {
            if (predicate.test(t)) {
                out.add(t);
            }
        }
        return out;
    }

    public static <T> T biggestMatching(Collection<T> collection, Predicate<T> predicate, Comparator<T> comparator) {
        T largest = null;
        for (T t : collection) {
            if (predicate.test(t)) {
                if (largest == null || comparator.compare(t, largest) > 0) {
                    largest = t;
                }
            }
        }
        return largest;
    }

    public static <T> boolean contains(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getSpawnEggOfEntity(ResourceLocation entity) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entity);

        for (Item registryItem : BuiltInRegistries.ITEM) {
            if (registryItem instanceof SpawnEggItem egg && egg.spawnsEntity(null, entityType)) {
                return egg.getDefaultInstance();
            }
        }
        return DEFAULT_SPAWN_EGG;
    }

    public static String getEntityOfSpawnEggForTooltip(ItemStack item) {
        String entityName = "";
        if (item.getItem() instanceof SpawnEggItem egg) {
            EntityType<?> entityType = egg.getType(null);
            entityName = entityType.getDescription().getString();
        }
        return entityName;
    }

    public static String getFluidFromBucketForTooltip(ItemStack item) {
        String itemName = BuiltInRegistries.ITEM.getKey(item.getItem()).toString();
        String liquidName = itemName.replace("_bucket", "");

        return getFluidName(new ResourceLocation(liquidName));
    }

    public static String getFluidName(ResourceLocation input) {
        Fluid fluid = BuiltInRegistries.FLUID.get(input);
        return getTranslationKey(fluid);
    }

    @Environment(EnvType.CLIENT)
    public static String getTranslationKey(Fluid fluid) {
        String translationKey;

        if (fluid == Fluids.EMPTY) {
            translationKey = "";
        } else if (fluid == Fluids.WATER) {
            translationKey = "block.minecraft.water";
        } else if (fluid == Fluids.LAVA) {
            translationKey = "block.minecraft.lava";
        } else {
            ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
            String key = Util.makeDescriptionId("block", id);
            String translated = I18n.get(key);
            translationKey = translated.equals(key) ? Util.makeDescriptionId("fluid", id) : key;
        }

        return translationKey;
    }

    public static double log(int base, int value) {
        return Math.log(value) / Math.log(base);
    }

    public static boolean removeItems(Inventory inventory, Item item, int amount) {
        if (inventory.countItem(item) < amount) return false;

        for (ItemStack stack : inventory.items) {
            if (stack.getItem().equals(item)) {
                int count = stack.getCount();
                if (count >= amount) {
                    stack.setCount(count - amount);
                    break;
                }
                stack.setCount(0);
                amount -= count;
            }
        }

        return true;
    }

    public static String removeBrackets(String text) {
        return text.replaceAll("[\\[\\]]", "");
    }

    public static String adjustStringToWidth(String input, int maxWidth) {
        Font fontRenderer = Minecraft.getInstance().font;

        if (fontRenderer.width(input) <= maxWidth) return input;

        StringBuilder truncatedString = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            String currentCharAsString = String.valueOf(currentChar);
            if (fontRenderer.width(truncatedString + currentCharAsString + "...") <= maxWidth) {
                truncatedString.append(currentCharAsString);
            } else {
                break;
            }
        }

        return truncatedString.append("...").toString();
    }

    public static Component getMissionMessage(ClientMissionData.ClientsideActiveMission mission, ChatFormatting color, Object... args) {
        String key = "missions.menu.main.tooltip.task." + mission.missionType();
        String translation = I18n.get(key).replace("Format error: ", "");

        if (translation.equals(key) && isAddonMission(mission)) {
            translation = mission.missionString().getString();
        }

        String[] parts = translation.split("%s", -1);

        if (parts.length == 0) {
            return Component.translatable(key);
        }

        MutableComponent result = Component.literal("");

        for (int i = 0; i < parts.length; i++) {
            if (i < args.length) {
                result.append(Component.translatable(parts[i]).setStyle(Style.EMPTY.withColor(color)));
                result.append(Component.translatable(args[i].toString()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)));
            } else {
                result.append(Component.translatable(parts[i]).setStyle(Style.EMPTY.withColor(color)));
            }
        }

        return result;
    }

    @Environment(EnvType.CLIENT)
    public static boolean isAddonMission(ClientMissionData.ClientsideActiveMission mission) {
        for (MissionType type : MissionTypeRegistry.INSTANCE.getAllTypes()) {
            if (type.id().equals(mission.missionType())) {
                return false;
            }
        }
        return true;
    }

    public static void executeCommandAsServer(String command) {
        MinecraftServer minecraftServer = MinecraftServerSupplier.getServer();
        CommandSourceStack commandSource = minecraftServer.createCommandSourceStack();
        Commands commandManager = minecraftServer.getCommands();

        ParseResults<CommandSourceStack> parseResults = commandManager.getDispatcher().parse(command, commandSource.withSuppressedOutput());

        MinecraftServerSupplier.getServer().getCommands().performCommand(parseResults, command);
    }

    public static int getTotalMissions() {
        return getClientStat(ModStats.EASY_MISSIONS_COMPLETED) + getClientStat(ModStats.NORMAL_MISSIONS_COMPLETED) + getClientStat(ModStats.HARD_MISSIONS_COMPLETED);
    }

    public static int getClientStat(ResourceLocation stat) {
        return Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(stat));
    }

    public static <T> T getRandomEntry(Collection<T> collection) {
        int num = (int) (Math.random() * collection.size());
        for (T t : collection) if (--num < 0) return t;
        throw new AssertionError();
    }

    public static <T> T getRandomEntry(List<T> collection) {
        return collection.get(RANDOM.nextInt(0, collection.size()));
    }

    public static <T> T getRandomEntry(Map<?, T> map) {
        return getRandomEntry(map.values());
    }
}
