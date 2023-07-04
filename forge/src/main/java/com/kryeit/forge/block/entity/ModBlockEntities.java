package com.kryeit.forge.block.entity;

import com.kryeit.Main;
import com.kryeit.forge.block.ModBlocks;
import com.kryeit.forge.block.entity.custom.ExchangeATMBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Main.MOD_ID);


    public static final RegistryObject<BlockEntityType<ExchangeATMBlockEntity>> EXCHANGE_ATM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("exchange_atm_block_entity", () ->
                    BlockEntityType.Builder.of(ExchangeATMBlockEntity::new,
                            ModBlocks.EXCHANGE_ATM.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
