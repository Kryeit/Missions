package com.kryeit.registry;

import com.kryeit.content.jar_of_tips.JarOfTipsProjectile;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectileRenderer;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static com.kryeit.Missions.REGISTRATE;

public class ModEntityTypes {

    public static final EntityEntry<JarOfTipsProjectile> JAR_OF_TIPS_PROJECTILE =
            register("jar_of_tips_projectile", JarOfTipsProjectile::new, () -> JarOfTipsProjectileRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, JarOfTipsProjectile::build).register();
    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String id, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
                                                                         NonNullConsumer<FabricEntityTypeBuilder<T>> propertyBuilder) {
        return (CreateEntityBuilder<T, ?>) REGISTRATE
                .entity(id, factory, group)
                .properties(b -> b.trackRangeChunks(range)
                        .trackedUpdateRate(updateFrequency)
                        .forceTrackedVelocityUpdates(sendVelocity))
                .properties(propertyBuilder)
                .properties(b -> {
                    if (immuneToFire)
                        b.fireImmune();
                })
                .renderer(renderer);
    }

    public static void register() {
    }
}
