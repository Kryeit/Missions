package com.kryeit.client.screen.button;

import com.kryeit.client.ClientMissionData;
import com.kryeit.client.screen.MissionScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MissionButton extends Button {
    public static final ResourceLocation GUI_WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation ADVANCEMENT_WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
    private final boolean completed;
    private final ItemStack item;

    protected final ClientMissionData.ClientsideActiveMission mission;

    public MissionButton(int x, int y, Component message, ClientMissionData.ClientsideActiveMission mission, OnPress onPress) {
        super(x, y, 200, 20, message, onPress, Button.DEFAULT_NARRATION);
        this.completed = mission.isCompleted();
        this.item = mission.previewItem();
        this.mission = mission;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderButtonTexture(guiGraphics);
        drawText(guiGraphics);
        if (isHoveredOrFocused()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, MissionScreen.getTooltip(mission), Optional.empty(), mouseX, mouseY);
        }
    }

    public void drawText(GuiGraphics guiGraphics) {
        int color = completed ? 0x29413c : mission.difficulty().color();
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawCenteredString(font, this.getMessage(), this.getX() + this.width / 2 + 11, this.getY() + (this.height - 8) / 2, color);
    }

    public void renderButtonTexture(GuiGraphics guiGraphics) {
        //Minecraft minecraft = Minecraft.getInstance();
        //minecraft.getTextureManager().bindForSetup(GUI_WIDGETS);

        int u = 0;
        int v = 66;

        if (isHovered) {
            v += 20;
        }

        int buttonWidth = 200;
        int buttonHeight = 20;

        int x = this.getX();
        int y = this.getY();

        //RenderSystem.setShaderTexture(0, GUI_WIDGETS);
        guiGraphics.blit(GUI_WIDGETS, x, y, u, v, buttonWidth, buttonHeight, 256, 256);
        renderItem(guiGraphics);
    }


    public void renderItem(GuiGraphics guiGraphics) {
        renderBelowItem(guiGraphics);
        int textureX = getX() + width / 2 - 92;
        int textureY = getY() + height / 2 - 8;
        guiGraphics.renderItem(item, textureX, textureY);
    }


    public void renderBelowItem(GuiGraphics guiGraphics) {
        //Minecraft minecraft = Minecraft.getInstance();
        //minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int v = 154;

        if (isHovered || mission.isCompleted())
            v -= 26;

        int u = switch (mission.difficulty()) {
            case NORMAL -> 26 * 2;
            case HARD -> 26;
            default -> 0;
        };

        int textureSize = 26;

        int x = this.getX() + 3;
        int y = this.getY() - 3;

        //RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        guiGraphics.blit(ADVANCEMENT_WIDGETS, x, y, u, v, textureSize, textureSize, 256, 256);
    }
}