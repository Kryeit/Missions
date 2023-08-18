package com.kryeit.coins;

import com.kryeit.Main;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Coins {

    public static List<ItemStack> getCoins() {
        return Main.getConfig().exchange();
    }

    public static ItemStack getExchange(ItemStack itemStack, boolean toBigger) {
        int index = getCoins().indexOf(itemStack);
        if (index < 1 || index == getCoins().size() - 1) return null;
        index += toBigger ? 1 : -1;
        return new ItemStack(getCoins().get(index).getItem(), 1);
    }
}
