package com.kryeit.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Thin wrapper around {@link Component#translatableWithFallback} so server-built text is
 * localization-ready: clients that have the lang key (mod installed or a resource pack)
 * see their language; everyone else (vanilla clients) sees the English fallback.
 *
 * Component args are resolved on the client too, so passing item/entity name Components
 * localizes them via the client's own Minecraft/Create lang.
 */
public final class Lang {
    private Lang() {}

    public static MutableComponent t(String key, String fallback, Object... args) {
        return Component.translatableWithFallback(key, fallback, args);
    }
}
