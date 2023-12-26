package com.kryeit.multiloader.forge;

import com.kryeit.multiloader.Env;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;

public class EnvImpl {
    @ApiStatus.Internal
    public static Env getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? Env.CLIENT : Env.SERVER;
    }
}
