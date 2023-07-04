package com.kryeit.fabric;

import com.kryeit.PlatformSpecific;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformSpecificImpl extends PlatformSpecific {
    public static boolean isClient() {
        // For Fabric
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
