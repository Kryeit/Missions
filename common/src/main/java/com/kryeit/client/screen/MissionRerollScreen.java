package com.kryeit.client.screen;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class MissionRerollScreen extends Screen {
    private final int missionIndex;

    public MissionRerollScreen(int missionIndex) {
        super(new TextComponent("Mission Reroll GUI"));
        this.missionIndex = missionIndex;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int buttonWidth = 100;
        int buttonHeight = 20;

        int startX = centerX - (buttonWidth * 2 + 5) / 2;

        this.addRenderableWidget(new Button(startX, centerY, buttonWidth, buttonHeight, new TextComponent("Close"), button -> close()));

        this.addRenderableWidget(new Button(startX + buttonWidth + 5, centerY, buttonWidth, buttonHeight, new TextComponent("Reroll"), button -> {
            ClientsideMissionPacketUtils.requestReroll(missionIndex);
            close();
        }));
    }

    private static void close() {
        Minecraft.getInstance().setScreen(new MissionScreen());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
