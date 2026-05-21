package com.kryeit.registry.forge;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

public class ModPondersImpl {
    public static ResourceLocation getId(RegistryEntry<?, ?> entry) {
        return entry.getId();
    }
}
