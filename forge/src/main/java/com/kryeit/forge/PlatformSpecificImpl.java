package com.kryeit.forge;

import com.kryeit.PlatformSpecific;
import net.minecraftforge.fml.loading.FMLLoader;

public class PlatformSpecificImpl extends PlatformSpecific {
    public static boolean isClient() {
        // For Forge
        return FMLLoader.getDist().isClient();
    }
}
