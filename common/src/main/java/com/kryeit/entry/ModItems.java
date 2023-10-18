package com.kryeit.entry;

import com.kryeit.Main;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;


public class ModItems {

    public static final CreativeModeTab CREATIVE_MODE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ModBlocks.EXCHANGE_ATM.asItem()))
            .title(Component.translatable("itemGroup.missions"))
            .displayItems((tab, consumer) -> {
                consumer.accept(new ItemStack(ModBlocks.EXCHANGE_ATM.asItem()));
            })
            .build();

    public static void register(String name, CreativeModeTab tab) {
        ResourceLocation id = new ResourceLocation(Main.MOD_ID, name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    public static void register() {

    }
}
