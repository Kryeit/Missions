package com.kryeit.coins;

import com.kryeit.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Coins {

    public static List<ItemStack> getCoins() {
        return List.of(
                Utils.getItem(new ResourceLocation("createdeco:copper_coin")),
                Utils.getItem(new ResourceLocation("createdeco:iron_coin")),
                Utils.getItem(new ResourceLocation("createdeco:gold_coin"))
        );
    }

    public static ItemStack getExchange(ItemStack itemStack, Boolean toBigger) {
        int index = getCoins().indexOf(itemStack);
        if (index == 0 || index == getCoins().size() - 1) return null;
        index += toBigger ? 1 : -1;
        return new ItemStack(getCoins().get(index).getItem(), 1);
    }
}
