package com.kryeit;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformSpecific {
    @ExpectPlatform
    public static boolean isClient() {
        throw new AssertionError();
    }
}
