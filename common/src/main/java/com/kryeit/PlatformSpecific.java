package com.kryeit;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public class PlatformSpecific {
    @ExpectPlatform
    public static boolean isClient() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable ResourceLocation getResourceLocation(Item item) {
        throw new AssertionError();
    }
}
