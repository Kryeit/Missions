package com.kryeit.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModTags {

    public static final TagKey<Item> DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "drinks"));
    public static final TagKey<Item> BREWING_DRINKS = TagKey.create(Registries.ITEM, new ResourceLocation("brewinandchewin", "drinks"));



    public static void register() {

    }

    public static boolean isDrink(ItemStack item) {
        return item.is(DRINKS);
    }

}
