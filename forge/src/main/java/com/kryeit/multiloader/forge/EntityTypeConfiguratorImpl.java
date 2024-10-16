package com.kryeit.multiloader.forge;

import com.kryeit.multiloader.EntityTypeConfigurator;
import net.minecraft.world.entity.EntityType;

// Code from https://github.com/Layers-of-Railways/Railway
public class EntityTypeConfiguratorImpl extends EntityTypeConfigurator {
    private final EntityType.Builder<?> builder;

    protected EntityTypeConfiguratorImpl(EntityType.Builder<?> builder) {
        this.builder = builder;
    }

    public static EntityTypeConfigurator of(Object builder) {
        if (builder instanceof EntityType.Builder<?> fabricBuilder)
            return new EntityTypeConfiguratorImpl(fabricBuilder);
        throw new IllegalArgumentException("builder must be an EntityType.Builder");
    }

    @Override
    public EntityTypeConfigurator size(float width, float height) {
        builder.sized(width, height);
        return this;
    }

    @Override
    public EntityTypeConfigurator fireImmune() {
        builder.fireImmune();
        return this;
    }
}
