package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JarOfTipsProjectileRenderer extends EntityRenderer<JarOfTipsProjectile> {

    public JarOfTipsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(JarOfTipsProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight) {
        ItemStack itemStack = ModBlocks.JAR_OF_TIPS.asStack();

        // The render dispatcher has ALREADY translated the pose to the entity's interpolated,
        // camera-relative position. Render at the origin (matching vanilla ThrownItemRenderer);
        // subtracting the camera position here again would double the offset and make the jar
        // appear far from where it actually is (invisible during flight).
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.15D, 0.0D); // lift off the entity's feet so the block sits centered
        poseStack.scale(1.6F, 1.6F, 1.6F);      // GROUND context is small; scale up so the flying jar is clearly visible
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(JarOfTipsProjectile entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
