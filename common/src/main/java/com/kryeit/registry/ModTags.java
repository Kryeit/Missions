package com.kryeit.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModTags {

    public static final TagKey<Item> DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "drinks"));
    public static final TagKey<Item> BREWING_DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("brewinandchewin", "drinks"));

    public static final TagKey<Item> CREATE_SCAFFOLDINGS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "scaffoldings"));
    public static final TagKey<Item> CREATE_LADDERS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "ladders"));
    public static final TagKey<Item> CREATE_BARS = TagKey.create(Registries.ITEM, new ResourceLocation("create", "bars"));

    public static final TagKey<Item> CREATEDECO_LADDERS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "ladders"));
    public static final TagKey<Item> CREATEDECO_BARS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "bars"));
    public static final TagKey<Item> CREATEDECO_CATWALKS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "catwalks"));
    public static final TagKey<Item> CREATEDECO_HULLS = TagKey.create(Registries.ITEM, new ResourceLocation("createdeco", "hulls"));





    public static void register() {

    }

    public static boolean isDrink(ItemStack item) {
        return item.is(DRINKS);
    }

}
