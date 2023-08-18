package com.kryeit.entry;

import com.kryeit.content.atm.ExchangeATMBlockEntity;
import com.kryeit.content.atm.ExchangeATMInstance;
import com.kryeit.content.atm.ExchangeATMRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.kryeit.Main.REGISTRATE;


public class ModBlockEntities {

    public static final BlockEntityEntry<ExchangeATMBlockEntity> EXCHANGE_ATM =
            REGISTRATE.blockEntity("exchange_atm", ExchangeATMBlockEntity::new)
            .instance(() -> ExchangeATMInstance::new)
            .validBlocks(ModBlocks.EXCHANGE_ATM)
            .renderer(() -> ExchangeATMRenderer::new)
            .register();

    public static void register() {}

}
