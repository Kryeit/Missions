package com.kryeit.forge;

import com.kryeit.PlatformSpecific;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLLoader;

public class PlatformSpecificImpl extends PlatformSpecific {
    public static boolean isClient() {
        return FMLLoader.getDist().isClient();
    }

    public static ResourceLocation getResourceLocation(Item item) {
        return item.getRegistryName();
    }
}
