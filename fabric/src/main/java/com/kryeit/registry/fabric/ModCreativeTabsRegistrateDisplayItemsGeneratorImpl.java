package com.kryeit.registry.fabric;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import static com.kryeit.Missions.REGISTRATE;

// Code from https://github.com/Layers-of-Railways/Railway
public class ModCreativeTabsRegistrateDisplayItemsGeneratorImpl {
    public static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
        return REGISTRATE.isInCreativeTab(entry, tab);
    }
}
