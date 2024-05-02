package com.kryeit.coins;

import com.kryeit.Missions;
import com.kryeit.missions.config.ConfigReader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;


public class Coins {

    public static List<ItemStack> getCoins() {
        return Optional.ofNullable(Missions.getConfig())
                .map(ConfigReader::exchange)
                .orElseThrow(() -> new IllegalStateException("Config is not available"));
    }

    public static ItemStack getCoin(int index) {
        List<ItemStack> coins = getCoins();
        if (index >= 0 && index < coins.size()) {
            return coins.get(index);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCoin(ItemStack item) {
        return getCoin(indexOf(item));
    }

    public static int indexOf(ItemStack itemStack) {
        Item item = itemStack.getItem();
        List<ItemStack> coins = getCoins();
        for (int index = 0; index < coins.size(); index++) {
            if (coins.get(index).getItem().equals(item)) {
                return index;
            }
        }
        return -1;
    }

    public static ItemStack getExchange(ItemStack itemStack, boolean toBigger) {
        int index = indexOf(itemStack);
        if (index == -1) {
            return ItemStack.EMPTY;
        }
        int newIndex = toBigger ? index + 1 : index - 1;
        if (newIndex < 0 || newIndex >= getCoins().size()) {
            return ItemStack.EMPTY;
        }
        ItemStack targetStack = getCoins().get(newIndex);
        return new ItemStack(targetStack.getItem(), toBigger ? 1 : getCoins().get(index).getCount());
    }

    // Check if an ItemStack is a recognized coin
    public static boolean isCoin(ItemStack itemStack) {
        return indexOf(itemStack) != -1;
    }
}

