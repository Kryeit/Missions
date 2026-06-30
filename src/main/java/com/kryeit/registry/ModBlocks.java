package com.kryeit.registry;

import com.kryeit.Missions;
import com.kryeit.content.exchanger.MechanicalExchangerBlock;
import com.kryeit.content.jar_of_tips.JarOfTipsBlock;
import com.kryeit.content.jar_of_tips.JarOfTipsItem;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {

    public static void registerStresses() {
        BlockStressValues.IMPACTS.register(MECHANICAL_EXCHANGER.get(), () -> 32.0);
    }

    public static final BlockEntry<MechanicalExchangerBlock> MECHANICAL_EXCHANGER = Missions.registrate()
            .block("mechanical_exchanger", MechanicalExchangerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::translucent)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(customItemModel())
            .register();

    public static final BlockEntry<JarOfTipsBlock> JAR_OF_TIPS = Missions.registrate()
            .block("jar_of_tips", JarOfTipsBlock::new)
            .properties(p -> p
                    .strength(0.0F, 0.0F)
                    .noOcclusion())
            .addLayer(() -> RenderType::translucent)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(JarOfTipsItem::new)
            .build()
            .register();

    public static void register() {}

}
