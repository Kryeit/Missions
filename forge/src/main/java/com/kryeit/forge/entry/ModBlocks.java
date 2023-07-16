package com.kryeit.forge.entry;

import com.kryeit.forge.content.block.ExchangeATMBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MaterialColor;

import static com.kryeit.forge.MainForge.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {

    public static final BlockEntry<ExchangeATMBlock> EXCHANGE_ATM =
        REGISTRATE.block("exchange_atm", ExchangeATMBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.METAL))
            .transform(BlockStressDefaults.setImpact(32.0))
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {}

}