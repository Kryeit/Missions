package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MissionButton extends Button {

    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/mission_button.png");
    public MissionButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        // Call the super method to render the button's text over the image
        super.renderButton(matrices, mouseX, mouseY, delta);
        renderButtonTexture(matrices, isHovered);
    }

    public void renderButtonTexture(PoseStack matrices, boolean isHovered) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(BUTTON_TEXTURE);

        int textureWidth = 200; // Replace with your texture's width
        int textureHeight = 20; // Replace with your texture's height

        //     // Increase the size if the button is hovered
        //     if (isHovered) {
        //         textureWidth += 10;
        //         textureHeight += 2;
        //     }

        int x = this.x + (this.width - textureWidth) / 2;
        int y = this.y + (this.height - textureHeight) / 2;

        RenderSystem.setShaderTexture(0, BUTTON_TEXTURE);
        blit(matrices, x, y, 0, 0, textureWidth, textureHeight);
    }


}
