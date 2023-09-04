package com.kryeit.client.screen;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class MissionRerollScreen extends Screen {
    private static final Component TITLE = new TranslatableComponent("missions.menu.reroll.title");
    private static final Component REROLL = new TranslatableComponent("missions.menu.reroll.reroll");
    private static final Component CLOSE = new TranslatableComponent("missions.menu.close");
    private final int missionIndex;

    public MissionRerollScreen(int missionIndex) {
        super(TITLE);
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

        this.addRenderableWidget(new Button(startX, centerY, buttonWidth, buttonHeight, CLOSE, button -> close()));

        this.addRenderableWidget(new Button(startX + buttonWidth + 5, centerY, buttonWidth, buttonHeight, REROLL, button -> {
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
