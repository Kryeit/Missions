package com.kryeit.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

// Code from https://github.com/Layers-of-Railways/Railway
public abstract class EntityTypeConfigurator {
    @ExpectPlatform
    public static EntityTypeConfigurator of(Object builder) {
        throw new AssertionError();
    }

    public abstract EntityTypeConfigurator size(float width, float height);
    public abstract EntityTypeConfigurator fireImmune();
}
