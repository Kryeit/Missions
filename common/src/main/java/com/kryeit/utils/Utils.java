package com.kryeit.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utils {
    private static final ItemStack DEFAULT_SPAWN_EGG = BuiltInRegistries.ITEM.get(new ResourceLocation("phantom_spawn_egg")).getDefaultInstance();

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

    public static ItemStack getSpawnEggOfEntity(ResourceLocation entity) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entity);

        for (Item registryItem : BuiltInRegistries.ITEM) {
            if (registryItem instanceof SpawnEggItem egg && egg.spawnsEntity(null, entityType)) {
                return egg.getDefaultInstance();
            }
        }
        return DEFAULT_SPAWN_EGG;
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
        return text.replaceAll("\\[", "").replaceAll("\\]", "");
    }

    public static String adjustStringToWidth(String input, int maxWidth) {
        Font fontRenderer = Minecraft.getInstance().font;

        if (fontRenderer.width(input) <= maxWidth) return input;

        StringBuilder truncatedString = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            String currentCharAsString = String.valueOf(currentChar);
            if (fontRenderer.width(truncatedString.toString() + currentCharAsString + "...") <= maxWidth) {
                truncatedString.append(currentCharAsString);
            } else {
                break;
            }
        }

        return truncatedString.append("...").toString();
    }
}
