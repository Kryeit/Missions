package com.kryeit.client.screen;

import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.button.InfoButton;
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

import java.util.List;
import java.util.Optional;

public class MissionScreen extends Screen {

    private final Runnable NO_TOOLTIP = () -> {
    };
    public Runnable activeTooltip = NO_TOOLTIP;
    private ClientMissionData data = null;

    public MissionScreen() {
        super(new TextComponent("Mission GUI"));
    }

    @Override
    protected void init() {
        super.init();

        ClientsideMissionPacketUtils.setMissionUpdateHandler(missionData -> data = missionData);
        ClientsideMissionPacketUtils.requestMissions();

        createCloseButton();
    }

    private void addMissions(ClientMissionData data) {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 5; // Space between buttons

        int leftX = (this.width / 2 - buttonWidth - spacing);
        int rightX = (this.width / 2 + spacing);

        List<ClientsideActiveMission> activeMissions = data.activeMissions();

        if (activeMissions.size() != 10) {
            Minecraft.getInstance().gui.getChat().addMessage(new TextComponent("Something wrong happened, you don't have 10 missions. Contact an admin"));
            return;
        }

        int missionsPerColumn = 5;

        for (int i = 0; i < missionsPerColumn; i++) {
            int y = (this.height - (missionsPerColumn * buttonHeight + (missionsPerColumn - 1) * spacing)) / 2 + i * (buttonHeight + spacing);

            // Use the mission's item as the button's title
            ClientsideActiveMission leftColumnMission = activeMissions.get(i);
            Component leftColumnTitle = leftColumnMission.titleString();

            // Left column
            this.addRenderableWidget(createMissionButton(leftX, y, leftColumnTitle, leftColumnMission, i));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ClientsideActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                Component rightColumnTitle = rightColumnMission.titleString();

                // Right column
                this.addRenderableWidget(createMissionButton(rightX, y, rightColumnTitle, rightColumnMission, i + missionsPerColumn));
            }
        }

        createInfoButton();
        createRewardButton(data.hasUnclaimedRewards());
    }

    private MissionButton createMissionButton(int x, int y, Component title, ClientsideActiveMission mission, int index) {
        Button.OnTooltip tooltip = (button, poseStack, mouseX, mouseY) -> renderTooltip(poseStack, getTooltip(mission), Optional.empty(), mouseX, mouseY);
        return new MissionButton(this, x, y, title, mission, tooltip,  button -> {
            Minecraft.getInstance().setScreen(new MissionRerollScreen(index));
        });
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        if (data != null) {
            addMissions(data);
            data = null;
        }

        drawCenteredString(matrices, Minecraft.getInstance().font, this.title, this.width / 2, 40, 0xFFFFFF);
        activeTooltip.run();
        activeTooltip = NO_TOOLTIP;
    }

    public List<Component> getTooltip(ClientsideActiveMission mission) {
        String progress = mission.isCompleted() ? "Completed" : mission.progress() + "/" + mission.requiredAmount();
        return List.of(
                new TextComponent("Mission Details")
                        .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),

                new TextComponent("Task: " + mission.missionString().getString())
                        .withStyle(ChatFormatting.WHITE),

                new TextComponent("Difficulty: " + mission.difficulty())
                        .withStyle(ChatFormatting.BLUE),

                new TextComponent("Item Required: " + mission.itemStack().getDisplayName().getString())
                        .withStyle(ChatFormatting.BLUE),

                new TextComponent("Your Progress: " + progress)
                        .withStyle(ChatFormatting.GREEN)
        );
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void createCloseButton() {
        int spacing = 5;
        int buttonWidth = 80;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = (this.width / 2 - buttonWidth - spacing);
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new Button(x, y, buttonWidth, buttonHeight, new TranslatableComponent("key.mission_gui.close"), button -> Minecraft.getInstance().setScreen(null)));
    }

    public void createInfoButton() {
        int x = this.width / 2 - 160;
        int y = this.height - 40;
        this.addRenderableWidget(new InfoButton(x, y));
    }

    public void createRewardButton(boolean rewardsAvailable) {
        int spacing = 5;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = (this.width / 2 + spacing);
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new RewardsButton(x, y, rewardsAvailable));
    }
}
