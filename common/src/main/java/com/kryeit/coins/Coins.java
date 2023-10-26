package com.kryeit.coins;

import com.kryeit.Main;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Coins {
    public static int EXCHANGE_RATE = 64;

    public static List<ItemStack> getCoins() {
        return Main.getConfig().exchange();
    }

    public static ItemStack getCoin(int index) {
        List<ItemStack> coins = getCoins();
        if (index >= 0 && index < coins.size()) {
            return coins.get(index);
        }
        return ItemStack.EMPTY;
    }

    public static int indexOf(ItemStack itemStack) {
        Item item = itemStack.getItem();
        int index = 0;
        boolean matched = false;
        for (ItemStack coin : getCoins()) {
            if (coin.getItem().equals(item)) {
                matched = true;
                break;
            }
            index++;
        }
        return matched ? index : -1;
    }

    public static ItemStack getExchange(ItemStack itemStack, boolean toBigger) {
        int index = indexOf(itemStack);
        if (index > getCoins().size() - (toBigger ? 2 : 1) || index < (toBigger ? 0 : 1)) return null;
        index += toBigger ? 1 : -1;
        return new ItemStack(getCoins().get(index).getItem(), toBigger ? 1 : EXCHANGE_RATE);
    }

}
