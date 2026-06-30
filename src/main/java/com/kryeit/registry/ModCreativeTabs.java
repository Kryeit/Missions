package com.kryeit.registry;

import com.kryeit.Missions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Single NeoForge creative tab. Registrate auto-populates it via
 * {@code REGISTRATE.defaultCreativeTab(getBaseTabKey())} (set in {@link Missions}).
 */
public class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Missions.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE = REGISTER.register("base", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Missions.MOD_ID))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> new ItemStack(ModBlocks.MECHANICAL_EXCHANGER.get()))
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return BASE.getKey();
    }
}
