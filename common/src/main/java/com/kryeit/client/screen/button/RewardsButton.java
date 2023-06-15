package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RewardsButton extends Button {

    private static final ResourceLocation CHEST_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/entity/chest/christmas.png");

    public RewardsButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);

        RenderSystem.setShaderTexture(0, CHEST_TEXTURE);
        int textureX = x + width / 2 - 8; // Center of the button, minus half the size of the texture
        int textureY = y + height / 2 - 8; // Center of the button, minus half the size of the texture
        blit(matrices, textureX, textureY, 0, 0, 16, 16, 16, 16);
    }
}
