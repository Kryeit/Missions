package com.kryeit.fabric;

import com.kryeit.PlatformSpecific;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class PlatformSpecificImpl extends PlatformSpecific {
    public static boolean isClient() {
        // For Fabric
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static ResourceLocation getResourceLocation(Item item) {
        return Registry.ITEM.getKey(item);
    }
}
