package com.kryeit.forge.block.entity;

import com.kryeit.Main;
import com.kryeit.forge.block.ExchangeATMInstance;
import com.kryeit.forge.block.ExchangeATMRenderer;
import com.kryeit.forge.block.ModBlocks;
import com.kryeit.forge.block.entity.custom.ExchangeATMBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.kryeit.forge.MainForge.REGISTRATE;


public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Main.MOD_ID);



    public static final BlockEntityEntry<ExchangeATMBlockEntity> EXCHANGE_ATM_BLOCK_ENTITY = REGISTRATE.blockEntity("exchange_atm", ExchangeATMBlockEntity::new)
            .instance(() -> ExchangeATMInstance::new, false)
            .validBlocks(ModBlocks.EXCHANGE_ATM_BLOCK)
            .renderer(() -> ExchangeATMRenderer::new)
            .register();

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
