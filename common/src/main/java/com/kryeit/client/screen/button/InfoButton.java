package com.kryeit.client.screen.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class InfoButton extends Button {

    public static final ResourceLocation INFO_ICON = new ResourceLocation("textures/gui/info_icon.png");
    private static final OnPress ON_PRESS = button -> {
    };

    public InfoButton(int x, int y) {
        super(x, y, 20, 20, new TextComponent(""), ON_PRESS);
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(INFO_ICON);
        blit(matrices, x, y, 0, 0, 20, 20, 20, 20);
    }
}
