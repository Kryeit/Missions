package com.kryeit.registry.forge;

import com.kryeit.registry.ModCreativeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.kryeit.Missions.REGISTRATE;

public class ModCreativeTabsRegistrateDisplayItemsGeneratorImpl {
    public static boolean isInCreativeTab(RegistryEntry<?, ?> entry, ResourceKey<CreativeModeTab> tab) {
        DeferredHolder<CreativeModeTab, CreativeModeTab> tabObject;
        if (tab == ModCreativeTabs.getBaseTabKey()) {
            tabObject = ModCreativeTabsImpl.MAIN_TAB;
        } else {
            tabObject = ModCreativeTabsImpl.MAIN_TAB;
        }
        return REGISTRATE.isInCreativeTab(entry, tabObject);
    }
}
