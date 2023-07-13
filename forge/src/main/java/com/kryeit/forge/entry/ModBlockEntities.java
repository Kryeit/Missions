package com.kryeit.forge.entry;

import com.kryeit.forge.content.block.ExchangeATMInstance;
import com.kryeit.forge.content.block.ExchangeATMRenderer;
import com.kryeit.forge.content.block.entity.custom.ExchangeATMBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.kryeit.forge.MainForge.REGISTRATE;


public class ModBlockEntities {
    public static final BlockEntityEntry<ExchangeATMBlockEntity> EXCHANGE_ATM =
            REGISTRATE.blockEntity("exchange_atm", ExchangeATMBlockEntity::new)
            .instance(() -> ExchangeATMInstance::new)
            .validBlocks(ModBlocks.EXCHANGE_ATM)
            .renderer(() -> ExchangeATMRenderer::new)
            .register();

    public static void register() {}

}
