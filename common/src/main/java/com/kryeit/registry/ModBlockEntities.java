package com.kryeit.registry;

import com.kryeit.Missions;
import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.MechanicalExchangerInstance;
import com.kryeit.content.exchanger.MechanicalExchangerRenderer;
import com.kryeit.content.jar_of_tips.JarOfTipsBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;


public class ModBlockEntities {

    public static final BlockEntityEntry<MechanicalExchangerBlockEntity> MECHANICAL_EXCHANGER = Missions.registrate()
            .blockEntity("mechanical_exchanger", MechanicalExchangerBlockEntity::new)
            .instance(() -> MechanicalExchangerInstance::new)
            .validBlocks(ModBlocks.MECHANICAL_EXCHANGER)
            .renderer(() -> MechanicalExchangerRenderer::new)
            .register();

    public static final BlockEntityEntry<JarOfTipsBlockEntity> JAR_OF_TIPS = Missions.registrate()
            .blockEntity("jar_of_tips", JarOfTipsBlockEntity::new)
            .validBlocks(ModBlocks.JAR_OF_TIPS)
            .register();

    public static void register() {}

}
