package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class InfoButton extends Button {

    public static final ResourceLocation INFO_ICON = new ResourceLocation(Main.MOD_ID, "textures/gui/info_icon.png");
    private static final OnPress ON_PRESS = button -> { };

    public InfoButton(int x, int y) {
        super(x, y, 20, 20, Component.empty(), ON_PRESS);
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(INFO_ICON);

        int textureSize = 20;

        RenderSystem.setShaderTexture(0, INFO_ICON);
        blit(matrices, this.x, this.y, 0, 0, textureSize, textureSize, 256, 256);
    }
}
