package com.kryeit.registry.forge;

import com.kryeit.registry.ModCreativeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

import static com.kryeit.Main.REGISTRATE;

public class ModCreativeTabsRegistrateDisplayItemsGeneratorImpl {
    public static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
        RegistryObject<CreativeModeTab> tabObject;
        if (tab == ModCreativeTabs.getBaseTabKey()) {
            tabObject = ModCreativeTabsImpl.MAIN_TAB;
        } else {
            tabObject = ModCreativeTabsImpl.MAIN_TAB;
        }
        return REGISTRATE.isInCreativeTab(entry, tabObject);
    }
}
