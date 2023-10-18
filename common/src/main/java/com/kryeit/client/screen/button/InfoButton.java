package com.kryeit.client.screen.button;

import com.kryeit.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InfoButton extends Button {

    public static final ResourceLocation INFO_ICON = new ResourceLocation(Main.MOD_ID, "textures/gui/info_icon.png");
    private static final OnPress ON_PRESS = button -> { };

    public InfoButton(int x, int y) {
        super(x, y, 20, 20, Component.empty(), ON_PRESS, Button.DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        //Minecraft minecraft = Minecraft.getInstance();
        //minecraft.getTextureManager().bindForSetup(INFO_ICON);

        int textureSize = 20;

        guiGraphics.blit(INFO_ICON, this.getX(), this.getY(), 0, 0, textureSize, textureSize, 256, 256);
    }
}
