package com.kryeit.registry.fabric;

import com.kryeit.Missions;
import com.kryeit.registry.ModCreativeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

import static com.kryeit.Missions.REGISTRATE;
import static com.kryeit.registry.ModBlocks.MECHANICAL_EXCHANGER;

public class ModCreativeTabsImpl {
    private static final ModCreativeTabs.TabInfo MAIN_TAB = register("main",
            () -> FabricItemGroup.builder()
                    .title(Component.literal("Create: Missions"))
                    .icon(() -> MECHANICAL_EXCHANGER.asStack())
                    .displayItems(new ModCreativeTabs.RegistrateDisplayItemsGenerator(ModCreativeTabs.Tabs.MAIN))
                    .build());

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.tab();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.key();
    }

    private static ModCreativeTabs.TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = new ResourceLocation(Missions.MOD_ID, name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new ModCreativeTabs.TabInfo(key, tab);
    }

    public static void useBaseTab() {
        REGISTRATE.useCreativeTab(ModCreativeTabs.getBaseTabKey());
    }
}
