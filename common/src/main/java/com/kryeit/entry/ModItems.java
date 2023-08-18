package com.kryeit.entry;

import com.kryeit.Main;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;


public class ModItems {
    public static final CreativeModeTab mainCreativeTab = new CreativeModeTab(CreativeModeTab.TABS.length - 1, Main.MOD_ID) {
        @Override
        @Nonnull
        public ItemStack makeIcon() {
            return ModBlocks.EXCHANGE_ATM.asStack();
        }
    };

    public static void register() {
    }
}
