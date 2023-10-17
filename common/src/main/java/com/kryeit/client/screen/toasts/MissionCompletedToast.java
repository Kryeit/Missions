package com.kryeit.client.screen.toasts;

import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

import static com.kryeit.client.screen.button.MissionButton.ADVANCEMENT_WIDGETS;

public class MissionCompletedToast implements Toast {
    private long firstDrawTime;

    private final ClientsideActiveMission mission;

    public MissionCompletedToast(ClientsideActiveMission mission) {
        this.mission = mission;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long delta) {
        if (this.firstDrawTime == 0L) {
            this.firstDrawTime = delta;
        }

        renderBackground(guiGraphics, toastComponent);
        renderItem(guiGraphics, toastComponent);
        renderText(guiGraphics, toastComponent);

        return delta - this.firstDrawTime < 10_000L ? Visibility.SHOW : Visibility.HIDE;
    }

    public void renderBackground(GuiGraphics guiGraphics, ToastComponent toastComponent) {
        toastComponent.getMinecraft().getTextureManager().bindForSetup(TEXTURE);
        RenderSystem.setShaderTexture(0, TEXTURE);

        toastComponent.blit(guiGraphics, 0, 0, 0, 0, this.width(), this.height());
    }

    public void renderText(GuiGraphics guiGraphics, ToastComponent toastComponent) {
        String title = Utils.adjustStringToWidth(mission.titleString().getString(), 125);

        // Title text
        Component titleText = Components.translatable(title).withStyle().withStyle(ChatFormatting.WHITE);
        toastComponent.getMinecraft().font.draw(guiGraphics, titleText, 30, 7, -1);

        // Description text
        Component descriptionText = Component.literal(mission.missionString().getString()).withStyle(ChatFormatting.WHITE);
        toastComponent.getMinecraft().font.draw(guiGraphics, descriptionText, 30, 18, -1);
    }

    public void renderItem(GuiGraphics guiGraphics, ToastComponent toastComponent) {
        renderBelowItem(guiGraphics, toastComponent);
        toastComponent.getMinecraft().getItemRenderer().renderAndDecorateItem(mission.previewItem(), 8, 8);
    }

    public void renderBelowItem(GuiGraphics guiGraphics, ToastComponent toastComponent) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int v = 128;

        int u = switch (mission.difficulty()) {
            case NORMAL -> 26 * 2;
            case HARD -> 26;
            default -> 0;
        };

        int textureSize = 26;

        RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        toastComponent.blit(guiGraphics, 3, 3, u, v, textureSize, textureSize);
    }

    public static void show(ClientsideActiveMission mission) {
        ToastComponent toastComponent = Minecraft.getInstance().getToasts();
        MissionCompletedToast toast = new MissionCompletedToast(mission);
        toastComponent.addToast(toast);
    }
}


