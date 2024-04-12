package com.kryeit.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModTags {

    // Common Tags
    public static final TagKey<Item> DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "drinks"));

    // Brewing and Chewing Tags
    public static TagKey<Item> BREWING_DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("brewinandchewin", "drinks"));

    // Create Tags
    public static final TagKey<Item> CREATE_SCAFFOLDINGS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "scaffoldings"));
    public static final TagKey<Item> CREATE_LADDERS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "ladders"));
    public static final TagKey<Item> CREATE_BARS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "bars"));

    // Create Deco Tags
    public static TagKey<Item> CREATEDECO_LADDERS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "ladders"));
    public static TagKey<Item> CREATEDECO_BARS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "bars"));
    public static TagKey<Item> CREATEDECO_CATWALKS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "catwalks"));
    public static TagKey<Item> CREATEDECO_HULLS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "hulls"));
    public static TagKey<Item> CREATEDECO_COINS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "coins"));



    public static void register() {

    }

    public static boolean isDrink(ItemStack item) {
        return item.is(DRINKS);
    }

}
