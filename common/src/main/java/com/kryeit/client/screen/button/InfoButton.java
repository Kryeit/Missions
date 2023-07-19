package com.kryeit.client.screen.button;

import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class InfoButton extends Button {

    public static final ResourceLocation INFO_ICON = new ResourceLocation("textures/gui/info_icon.png");

    public InfoButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, null);

    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(INFO_ICON);
        blit(matrices, x, y, 0, 0, 20, 20, 20, 20);
    }
}
