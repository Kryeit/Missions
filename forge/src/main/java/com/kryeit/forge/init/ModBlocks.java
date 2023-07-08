package com.kryeit.forge.init;

import com.kryeit.forge.block.ExchangeATMBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import static com.kryeit.forge.MainForge.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {

    public static final BlockEntry<ExchangeATMBlock> EXCHANGE_ATM_BLOCK = REGISTRATE.block("exchange_atm", ExchangeATMBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .transform(BlockStressDefaults.setCapacity(8.0))
            .transform(axeOrPickaxe())
            .simpleItem()
            .register();

    public static void register() {}

}
