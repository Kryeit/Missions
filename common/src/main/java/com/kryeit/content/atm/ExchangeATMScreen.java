package com.kryeit.content.atm;

import com.kryeit.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tropheusj.milk.mixin.BrewingStandScreenHandlerPotionSlotMixin;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExchangeATMScreen extends AbstractContainerScreen<ExchangeATMMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/gui/exchange_atm_gui.png");
    public ExchangeATMScreen(ExchangeATMMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            blit(poseStack, x + 102, y + 41, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
