package com.kryeit.entry;

import com.kryeit.Main;
import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.MechanicalExchangerInstance;
import com.kryeit.content.exchanger.MechanicalExchangerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.kryeit.Main.REGISTRATE;


public class ModBlockEntities {

    public static final BlockEntityEntry<MechanicalExchangerBlockEntity> MECHANICAL_EXCHANGER = Main.registrate()
            .blockEntity("mechanical_exchanger",MechanicalExchangerBlockEntity::new)
            .instance(() -> MechanicalExchangerInstance::new)
            .validBlocks(ModBlocks.MECHANICAL_EXCHANGER)
            .renderer(() -> MechanicalExchangerRenderer::new)
            .register();

    public static void register() {}

}
