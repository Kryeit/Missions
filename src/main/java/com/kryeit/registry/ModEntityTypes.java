package com.kryeit.registry;

import com.kryeit.content.jar_of_tips.JarOfTipsProjectile;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectileRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static com.kryeit.Missions.REGISTRATE;

public class ModEntityTypes {

    // Entity renderer is registered manually in MissionsClient (Registrate does not bind entity renderers).
    public static final EntityEntry<JarOfTipsProjectile> JAR_OF_TIPS_PROJECTILE =
            REGISTRATE.<JarOfTipsProjectile>entity("jar_of_tips_projectile", JarOfTipsProjectile::new, MobCategory.MISC)
                    .properties(b -> b.sized(.35f, .35f))
                    .lang("Jar of Tips")
                    .register();

    public static void register() {
    }
}
