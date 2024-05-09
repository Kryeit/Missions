package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

        poseStack.pushPose();

        // Calculate interpolated position for smoother rendering
        double x = entity.xo + (entity.getX() - entity.xo) * partialTicks;
        double y = entity.yo + (entity.getY() - entity.yo) * partialTicks;
        double z = entity.zo + (entity.getZ() - entity.zo) * partialTicks;

        // Move the rendering origin to the entity's position
        poseStack.translate(x - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x,
                y - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y,
                z - Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z);

        // Render the ItemStack
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(JarOfTipsProjectile entity) {
        return null;
    }
}
