package com.kryeit.forge.tab;

import com.kryeit.forge.init.ModBlocks;
import net.minecraft.world.item.ItemStack;

public class MissionsCreativeModeTab extends MissionsCreativeTabs {

    public MissionsCreativeModeTab() {
        super("base");
    }

    @Override
    public ItemStack makeIcon() {
        return ModBlocks.EXCHANGE_ATM_BLOCK.asStack();
    }

}

