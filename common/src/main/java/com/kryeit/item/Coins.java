package com.kryeit.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Coins {
    public static Item ironCoin() {
        return Registry.ITEM.get(new ResourceLocation("createdeco", "iron_coin"));
    }

    public static Item copperCoin() {
        return Registry.ITEM.get(new ResourceLocation("createdeco", "copper_coin"));
    }

    public static Item goldCoin() {
        return Registry.ITEM.get(new ResourceLocation("createdeco", "gold_coin"));
    }

    public static ItemStack getSmallerExchange(ItemStack coin) {
        Item coinType = coin.getItem();
        if(coinType.equals(goldCoin())) {
            return new ItemStack(copperCoin(),64);
        } else if(coinType.equals(copperCoin())) {
            return new ItemStack(ironCoin(),64);
        }
        return null;
    }

    public static ItemStack getBiggerExchange(ItemStack coins) {
        Item coinType = coins.getItem();
        int amount = coins.getCount();
        if(amount != 64) return null;
        if(coinType.equals(copperCoin())) {
            return new ItemStack(goldCoin(),1);
        } else if(coinType.equals(ironCoin())) {
            return new ItemStack(copperCoin(),1);
        }
        return null;
    }
}
