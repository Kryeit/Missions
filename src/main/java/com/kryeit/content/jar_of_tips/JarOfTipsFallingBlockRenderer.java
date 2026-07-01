package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * Renders the falling Jar of Tips. The jar block is ENTITYBLOCK_ANIMATED so the vanilla falling-block
 * renderer would skip it (and fall back to sand); this force-renders the jar's baked model instead.
 */
public class JarOfTipsFallingBlockRenderer extends EntityRenderer<JarOfTipsFallingBlockEntity> {

    private final BlockRenderDispatcher dispatcher;

    public JarOfTipsFallingBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(JarOfTipsFallingBlockEntity entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int light) {
        BlockState state = entity.getBlockState();
        if (state == null || !(state.getBlock() instanceof JarOfTipsBlock)) {
            state = ModBlocks.JAR_OF_TIPS.get().defaultBlockState();
        }

        pose.pushPose();
        pose.translate(-0.5D, 0.0D, -0.5D); // center the 1x1 block model on the entity
        BakedModel model = dispatcher.getBlockModel(state);
        RandomSource random = RandomSource.create(42L);
        for (RenderType renderType : model.getRenderTypes(state, random, ModelData.EMPTY)) {
            dispatcher.getModelRenderer().renderModel(pose.last(), buffers.getBuffer(renderType), state, model,
                    1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
        }
        pose.popPose();
        super.render(entity, yaw, partialTicks, pose, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(JarOfTipsFallingBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
