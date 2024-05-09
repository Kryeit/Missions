package com.kryeit.registry;

import com.kryeit.content.jar_of_tips.JarOfTipsProjectile;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectileRenderer;
import com.kryeit.multiloader.EntityTypeConfigurator;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Consumer;

import static com.kryeit.Missions.REGISTRATE;

public class ModEntityTypes {

    public static final EntityEntry<JarOfTipsProjectile> JAR_OF_TIPS_PROJECTILE = REGISTRATE.<JarOfTipsProjectile>entity("jar_of_tips_projectile", JarOfTipsProjectile::new, MobCategory.MISC)
            .renderer(() -> JarOfTipsProjectileRenderer::new)
            .properties(configure(c -> c.size(.35f, .35f)))
            .lang("Jar of Tips")
            .register();

    private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> consumer) {
        return builder -> consumer.accept(EntityTypeConfigurator.of(builder));
    }

    public static void register() {
    }
}
