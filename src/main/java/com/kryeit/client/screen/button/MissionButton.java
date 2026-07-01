package com.kryeit.client.screen.button;

import com.kryeit.client.ClientMissionData;
import com.kryeit.client.screen.MissionScreen;
import com.kryeit.missions.MissionDifficulty;
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

    public MissionButton(int x, int y, int width, Component message, ClientMissionData.ClientsideActiveMission mission, OnPress onPress) {
        super(x, y, width, 20, message, onPress, Button.DEFAULT_NARRATION);
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
        // Center the title in the space to the right of the item frame (which ends around x=25).
        int textCenterX = this.getX() + (27 + this.width) / 2;
        guiGraphics.drawCenteredString(font, this.getMessage(), textCenterX, this.getY() + (this.height - 8) / 2, color);
    }

    public void renderButtonTexture(GuiGraphics guiGraphics) {
        ResourceLocation sprite = isHovered ? BUTTON_HIGHLIGHTED : BUTTON_SPRITE;
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.width, this.height);
        renderItem(guiGraphics);
    }

    public void renderItem(GuiGraphics guiGraphics) {
        // Fixed 8px from the left edge (independent of button width).
        int itemX = getX() + 8;
        int itemY = getY() + height / 2 - 8;
        drawFrame(guiGraphics, itemX, itemY, mission.difficulty(), isHovered || completed);
        guiGraphics.renderItem(item, itemX, itemY);
    }

    /** Vanilla advancement frame around a 16x16 item: task=easy, goal=normal, challenge=hard. */
    public static void drawFrame(GuiGraphics guiGraphics, int itemX, int itemY, MissionDifficulty difficulty, boolean obtained) {
        guiGraphics.blitSprite(frameSprite(difficulty, obtained), itemX - 5, itemY - 5, 26, 26);
    }

    public static ResourceLocation frameSprite(MissionDifficulty difficulty, boolean obtained) {
        String type = switch (difficulty) {
            case EASY -> "task";
            case NORMAL -> "goal";
            case HARD -> "challenge";
        };
        return ResourceLocation.withDefaultNamespace("advancements/" + type + "_frame_" + (obtained ? "obtained" : "unobtained"));
    }
}
