package com.kryeit.multiloader.fabric;

import com.kryeit.multiloader.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

// Code from https://github.com/Layers-of-Railways/Railway
public class EnvImpl {
    @ApiStatus.Internal
    public static Env getCurrent() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Env.CLIENT : Env.SERVER;
    }
}
