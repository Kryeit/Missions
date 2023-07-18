package com.kryeit.client.screen;

import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.button.MissionButton;
import com.kryeit.client.screen.button.RewardsButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionScreen extends Screen {

    public final List<Runnable> tooltipRunnables = new ArrayList<>();

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
            Component leftColumnTitle = leftColumnMission.titleString();

            // Left column
            this.addRenderableWidget(new MissionButton(this, leftX, y, buttonWidth, buttonHeight, leftColumnTitle, leftColumnMission, button -> {
                // Button clicked
            }, (button, poseStack, mouseX, mouseY) -> {
                renderTooltip(poseStack, getTooltip(leftColumnMission), Optional.empty(), mouseX, mouseY);

            }));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ClientsideActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                Component rightColumnTitle = rightColumnMission.titleString();

                // Right column
                this.addRenderableWidget(new MissionButton(this, rightX, y, buttonWidth, buttonHeight, rightColumnTitle, rightColumnMission, button -> {
                    // Button clicked
                },  (button, poseStack, mouseX, mouseY) -> {
                    renderTooltip(poseStack, getTooltip(rightColumnMission), Optional.empty(), mouseX, mouseY);
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
        tooltipRunnables.forEach(Runnable::run);
        tooltipRunnables.clear();
    }

    public List<Component> getTooltip(ClientsideActiveMission mission) {
        return List.of(
                new TextComponent("Mission Details")
                        .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),

                new TextComponent("Task: " + mission.missionString().getString())
                        .withStyle(ChatFormatting.WHITE),

                new TextComponent("Item Required: " + mission.itemStack().getDisplayName().getString())
                        .withStyle(ChatFormatting.BLUE),

                new TextComponent("Amount Required: " + mission.requiredAmount())
                        .withStyle(ChatFormatting.BLUE),

                new TextComponent("Your Progress: " + mission.progress() + "/" + mission.requiredAmount())
                        .withStyle(ChatFormatting.GREEN)
        );
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void closeButton() {
        int spacing = 5;
        int buttonWidth = 80;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = (this.width / 2 - buttonWidth - spacing);
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new Button(x, y, buttonWidth, buttonHeight, new TranslatableComponent("key.mission_gui.close"),
                button -> Minecraft.getInstance().setScreen(null)));
    }

    public void rewardButton() {
        int spacing = 5;
        int buttonWidth = 80;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = (this.width / 2 + spacing);
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new RewardsButton(x, y, buttonWidth, buttonHeight, new TextComponent("   Rewards"),
                button -> ClientsideMissionPacketUtils.requestPayout()));
    }
}
