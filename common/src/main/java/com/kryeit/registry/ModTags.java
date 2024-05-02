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
    public static TagKey<Item> CREATEDECO_LADDERS = TagKey.create(Registries.ITEM, new ResourceLocation("numismatics", "ladders"));
    public static TagKey<Item> CREATEDECO_BARS = TagKey.create(Registries.ITEM, new ResourceLocation("numismatics", "bars"));
    public static TagKey<Item> CREATEDECO_CATWALKS = TagKey.create(Registries.ITEM, new ResourceLocation("numismatics", "catwalks"));
    public static TagKey<Item> CREATEDECO_HULLS = TagKey.create(Registries.ITEM, new ResourceLocation("numismatics", "hulls"));
    public static TagKey<Item> CREATEDECO_COINS = TagKey.create(Registries.ITEM, new ResourceLocation("numismatics", "coins"));

    // Railways Tags
    public static TagKey<Item> RAILWAYS_TRACKS = TagKey.create(Registries.ITEM, new ResourceLocation("railways", "tracks"));
    public static TagKey<Item> RAILWAYS_NARROW_TRACKS = TagKey.create(Registries.ITEM, new ResourceLocation("railways", "narrow_tracks"));
    public static TagKey<Item> RAILWAYS_WIDE_TRACKS = TagKey.create(Registries.ITEM, new ResourceLocation("railways", "wide_tracks"));

    // Farmer's Delight Tags
    public static TagKey<Item> FARMERSDELIGHT_DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("farmersdelight", "drinks"));

    public static void register() {

    }

    public static boolean isDrink(ItemStack item) {
        return item.is(DRINKS);
    }

}
