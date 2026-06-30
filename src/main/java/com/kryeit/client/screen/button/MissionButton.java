package com.kryeit.client.screen.button;

import com.kryeit.client.ClientMissionData;
import com.kryeit.client.screen.MissionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class MissionButton extends Button {
    // 1.21 split widgets.png into sprites; use the vanilla button sprites.
    private static final ResourceLocation BUTTON_SPRITE = ResourceLocation.withDefaultNamespace("widget/button");
    private static final ResourceLocation BUTTON_HIGHLIGHTED = ResourceLocation.withDefaultNamespace("widget/button_highlighted");

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
        if (isHovered) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, MissionScreen.getTooltip(mission), Optional.empty(), mouseX, mouseY);
        }
    }

    public void drawText(GuiGraphics guiGraphics) {
        int color = completed ? 0x29413c : mission.difficulty().color();
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawCenteredString(font, this.getMessage(), this.getX() + this.width / 2 + 11, this.getY() + (this.height - 8) / 2, color);
    }

    public void renderButtonTexture(GuiGraphics guiGraphics) {
        ResourceLocation sprite = isHovered ? BUTTON_HIGHLIGHTED : BUTTON_SPRITE;
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.width, this.height);
        renderItem(guiGraphics);
    }

    public void renderItem(GuiGraphics guiGraphics) {
        int itemX = getX() + width / 2 - 92;
        int itemY = getY() + height / 2 - 8;
        renderFrame(guiGraphics, itemX, itemY);
        guiGraphics.renderItem(item, itemX, itemY);
    }

    /** Draws a thin difficulty-colored frame around the mission item (green once completed). */
    private void renderFrame(GuiGraphics guiGraphics, int itemX, int itemY) {
        int color = 0xFF000000 | (completed ? 0x29C95B : mission.difficulty().color());
        int x0 = itemX - 1;
        int y0 = itemY - 1;
        int x1 = itemX + 17;
        int y1 = itemY + 17;
        guiGraphics.fill(x0, y0, x1, y0 + 1, color);
        guiGraphics.fill(x0, y1 - 1, x1, y1, color);
        guiGraphics.fill(x0, y0, x0 + 1, y1, color);
        guiGraphics.fill(x1 - 1, y0, x1, y1, color);
    }
}
