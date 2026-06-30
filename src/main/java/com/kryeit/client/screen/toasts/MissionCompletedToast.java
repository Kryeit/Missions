package com.kryeit.client.screen.toasts;

import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class MissionCompletedToast implements Toast {
    // 1.21 replaced toasts.png with sprites; reuse the vanilla advancement toast background sprite.
    private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("toast/advancement");

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

        renderBackground(guiGraphics);
        renderItem(guiGraphics);
        renderText(guiGraphics, toastComponent);

        return delta - this.firstDrawTime < 15_000L ? Visibility.SHOW : Visibility.HIDE;
    }

    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(BACKGROUND, 0, 0, this.width(), this.height());
    }

    public void renderText(GuiGraphics guiGraphics, ToastComponent toastComponent) {
        String title = Utils.adjustStringToWidth(mission.titleString().getString(), 125);

        Component titleText = Component.literal(title).withStyle(ChatFormatting.WHITE);
        guiGraphics.drawString(toastComponent.getMinecraft().font, titleText, 30, 7, -1);

        Component descriptionText = Component.literal(mission.missionString().getString()).withStyle(ChatFormatting.WHITE);
        guiGraphics.drawString(toastComponent.getMinecraft().font, descriptionText, 30, 18, -1);
    }

    public void renderItem(GuiGraphics guiGraphics) {
        renderFrame(guiGraphics);
        guiGraphics.renderItem(mission.previewItem(), 8, 8);
    }

    private void renderFrame(GuiGraphics guiGraphics) {
        int color = 0xFF000000 | mission.difficulty().color();
        guiGraphics.fill(7, 7, 25, 8, color);
        guiGraphics.fill(7, 24, 25, 25, color);
        guiGraphics.fill(7, 7, 8, 25, color);
        guiGraphics.fill(24, 7, 25, 25, color);
    }

    public static void show(ClientsideActiveMission mission) {
        ToastComponent toastComponent = Minecraft.getInstance().getToasts();
        MissionCompletedToast toast = new MissionCompletedToast(mission);
        toastComponent.addToast(toast);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1));
    }
}
