package com.kryeit.entry;

import com.kryeit.Main;
import com.kryeit.utils.ItemUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;


public class ModItems {
    public static final CreativeModeTab mainCreativeTab = new CreativeModeTab(ItemUtils.nextTabId(), Main.MOD_ID) {
        @Override
        @Nonnull
        public ItemStack makeIcon() { return Items.BEDROCK.getDefaultInstance(); }
    };

    public static void register() {}
}
