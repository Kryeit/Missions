package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MissionButton extends Button {
    public static final ResourceLocation GUI_WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation ADVANCEMENT_WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
    private final boolean completed;
    private final ItemStack item;
    protected final OnTooltip onTooltip;

    private final MissionScreen screen;

    public MissionButton(MissionScreen screen, int x, int y, int width, int height, Component message, boolean completed, ItemStack item, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, message, onPress);
        this.completed = completed;
        this.item = item;
        this.onTooltip = onTooltip;
        this.screen = screen;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderButtonTexture(matrices);
        renderItem(matrices);
        int color = completed ? 0x00FF00 : 0xFFFFFF;
        Font font = Minecraft.getInstance().font;
        AbstractWidget.drawCenteredString(matrices, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

        if (this.isMouseOver(mouseX, mouseY)) {
            screen.tooltipRunnables.add(() -> this.onTooltip.onTooltip(this, matrices, mouseX, mouseY));
        }
    }

    public void renderButtonTexture(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(GUI_WIDGETS);

        int textureWidth = 256;
        int textureHeight = 256;

        int u = 0;
        int v = 66;

        if(isHovered) {
            v = 86;
        }

        int buttonWidth = 200;
        int buttonHeight = 20;

        int x = this.x;
        int y = this.y;

        RenderSystem.setShaderTexture(0, GUI_WIDGETS);
        blit(matrices, x, y, u, v, buttonWidth, buttonHeight, textureWidth, textureHeight);
    }


    public void renderItem(PoseStack matrices) {
        renderBelowItem(matrices);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int textureX = x + width / 2 - 92;
        int textureY = y + height / 2 - 8; // Center of the button, minus half the size of the texture
        itemRenderer.renderGuiItem(item, textureX, textureY);
    }


    public void renderBelowItem(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int textureWidth = 256;
        int textureHeight = 256;

        int u = 0;
        int v = 128;

        if(isHovered) {
            u = 26;
        }

        int buttonWidth = 26;
        int buttonHeight = 26;

        int x = this.x + 3;
        int y = this.y - 3;

        RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        blit(matrices, x, y, u, v, buttonWidth, buttonHeight, textureWidth, textureHeight);
    }
}
