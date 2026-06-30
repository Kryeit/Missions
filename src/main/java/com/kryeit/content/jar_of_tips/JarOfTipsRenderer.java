package com.kryeit.content.jar_of_tips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * Renders the Jar of Tips block model (the block is ENTITYBLOCK_ANIMATED so the chunk does not draw it),
 * applying a short tilt "wobble" when an item is inserted, like a vanilla Decorated Pot.
 */
public class JarOfTipsRenderer implements BlockEntityRenderer<JarOfTipsBlockEntity> {

    private static final float WOBBLE_DURATION = 6f;
    private final BlockRenderDispatcher blockRenderer;

    public JarOfTipsRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(JarOfTipsBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        Level level = be.getLevel();
        if (level == null) return;
        BlockState state = be.getBlockState();
        if (!(state.getBlock() instanceof JarOfTipsBlock)) return;

        BakedModel model = blockRenderer.getBlockModel(state);

        pose.pushPose();

        float age = (float) (level.getGameTime() - be.wobbleStartedAtTick) + partialTick;
        if (be.wobbleStartedAtTick != Long.MIN_VALUE && age >= 0 && age < WOBBLE_DURATION) {
            float t = age / WOBBLE_DURATION;
            float angle = Mth.sin(t * (float) Math.PI) * (be.wobblePositive ? 4.0f : -6.0f);
            pose.translate(0.5, 0.0, 0.5);
            pose.mulPose(Axis.XP.rotationDegrees(angle));
            pose.mulPose(Axis.ZP.rotationDegrees(angle * 0.5f));
            pose.translate(-0.5, 0.0, -0.5);
        }

        RandomSource random = RandomSource.create(42L);
        for (RenderType renderType : model.getRenderTypes(state, random, ModelData.EMPTY)) {
            blockRenderer.getModelRenderer().renderModel(pose.last(), buffers.getBuffer(renderType), state, model,
                    1.0f, 1.0f, 1.0f, light, overlay, ModelData.EMPTY, renderType);
        }

        pose.popPose();
    }
}
