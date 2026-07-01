package com.kryeit.registry;

import com.kryeit.content.jar_of_tips.JarOfTipsFallingBlockEntity;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectile;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectileRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static com.kryeit.Missions.REGISTRATE;

public class ModEntityTypes {

    // Entity renderers are registered manually in MissionsClient (Registrate does not bind entity renderers).
    public static final EntityEntry<JarOfTipsProjectile> JAR_OF_TIPS_PROJECTILE =
            REGISTRATE.<JarOfTipsProjectile>entity("jar_of_tips_projectile", JarOfTipsProjectile::new, MobCategory.MISC)
                    .properties(b -> b.sized(.35f, .35f))
                    .lang("Jar of Tips")
                    .register();

    // The jar falls as its OWN falling-block entity (not vanilla FALLING_BLOCK) so a custom renderer can
    // draw the ENTITYBLOCK_ANIMATED jar model instead of vanilla falling back to a sand block.
    public static final EntityEntry<JarOfTipsFallingBlockEntity> JAR_OF_TIPS_FALLING_BLOCK =
            REGISTRATE.<JarOfTipsFallingBlockEntity>entity("jar_of_tips_falling_block", JarOfTipsFallingBlockEntity::new, MobCategory.MISC)
                    .properties(b -> b.sized(0.98f, 0.98f).updateInterval(1))
                    .register();

    public static void register() {
    }
}
