package com.kryeit.client.screen;

import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.button.MissionButton;
import com.kryeit.client.screen.button.RewardsButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class MissionScreen extends Screen {

    public MissionScreen() {
        super(new TextComponent("Mission GUI"));
    }

    @Override
    protected void init() {
        super.init();

        ClientsideMissionPacketUtils.setMissionUpdateHandler(this::addMissions);
        ClientsideMissionPacketUtils.requestMissions();

        closeButton();
    }

    private void addMissions(List<ClientsideActiveMission> activeMissions) {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 5; // Space between buttons

        int leftX = (this.width / 2 - buttonWidth - spacing);
        int rightX = (this.width / 2 + spacing);

        if (activeMissions.size() != 10) {
            Minecraft.getInstance().gui.getChat().addMessage(new TextComponent("Something wrong happened, you don't have 10 missions. Contact an admin"));
            return;
        }

        // Number of missions for each column
        int missionsPerColumn = 5;

        for (int i = 0; i < missionsPerColumn; i++) {
            int y = (this.height - (missionsPerColumn * buttonHeight + (missionsPerColumn - 1) * spacing)) / 2 + i * (buttonHeight + spacing);

            // Use the mission's item as the button's title
            ClientsideActiveMission leftColumnMission = activeMissions.get(i);
            Component leftColumnTitle = leftColumnMission.missionString();

            // Left column
            this.addRenderableWidget(new MissionButton(leftX, y, buttonWidth, buttonHeight, leftColumnTitle, leftColumnMission.isCompleted(), button -> {
                // Button clicked
            }));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ClientsideActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                Component rightColumnTitle = rightColumnMission.missionString();

                // Right column
                this.addRenderableWidget(new MissionButton(rightX, y, buttonWidth, buttonHeight, rightColumnTitle, rightColumnMission.isCompleted(), button -> {
                    // Button clicked
                }));
            }
        }

        rewardButton();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices, Minecraft.getInstance().font, this.title, this.width / 2, 40, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void closeButton() {
        int buttonWidth = 160;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int centerX = this.width / 2 - buttonWidth / 2; // Center of the screen, minus half the button's width
        int centerY = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new Button(centerX, centerY, buttonWidth, buttonHeight, new TranslatableComponent("key.mission_gui.close"),
                button -> Minecraft.getInstance().setScreen(null)));
    }

    public void rewardButton() {
        // Add the reward button at the bottom right
        int buttonWidth = 80;
        int buttonHeight = 20;
        int rightPadding = 50;
        int bottomPadding = 20;
        int x = this.width - buttonWidth - rightPadding;
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new RewardsButton(x, y, buttonWidth, buttonHeight, new TextComponent("   Rewards"),
                button -> ClientsideMissionPacketUtils.requestPayout()));
    }
}
