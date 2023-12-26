package com.kryeit.entry;

import com.kryeit.Main;
import com.kryeit.utils.Utils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import static com.kryeit.entry.ModBlocks.EXCHANGE_ATM;

public class ModCreativeTabs {

    public static final CreativeModeTab mainCreativeTab = new CreativeModeTab(Utils.nextTabId(), Main.MOD_ID) {
        @Override
        @Nonnull
        public ItemStack makeIcon() { return EXCHANGE_ATM.asStack(); }
    };
}