package com.kryeit.entry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public class ModItems {

    public static final CreativeModeTab CREATIVE_MODE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ModBlocks.EXCHANGE_ATM.asItem()))
            .title(Component.translatable("itemGroup.missions"))
            .displayItems((tab, consumer) -> {
                consumer.accept(new ItemStack(ModBlocks.EXCHANGE_ATM.asItem()));
            })
            .build();


    public static void register() {

    }
}
