package com.kryeit.client;

import com.kryeit.Missions;
import com.kryeit.content.exchanger.MechanicalExchangerRenderer;
import com.kryeit.content.jar_of_tips.JarOfTipsFallingBlockRenderer;
import com.kryeit.content.jar_of_tips.JarOfTipsProjectileRenderer;
import com.kryeit.content.jar_of_tips.JarOfTipsRenderer;
import com.kryeit.registry.ModBlockEntities;
import com.kryeit.registry.ModEntityTypes;
import com.kryeit.registry.ModPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only setup. We register entity/block-entity renderers explicitly here (Registrate does not
 * bind entity renderers, and explicit block-entity renderer registration is overwrite-safe), plus
 * the Ponder plugin. Menu screens and Flywheel visuals are still bound by Registrate.
 */
@EventBusSubscriber(modid = Missions.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MissionsClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PonderIndex.addPlugin(new ModPonderPlugin()));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.JAR_OF_TIPS_PROJECTILE.get(), JarOfTipsProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.JAR_OF_TIPS_FALLING_BLOCK.get(), JarOfTipsFallingBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JAR_OF_TIPS.get(), JarOfTipsRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MECHANICAL_EXCHANGER.get(), MechanicalExchangerRenderer::new);
    }
}
