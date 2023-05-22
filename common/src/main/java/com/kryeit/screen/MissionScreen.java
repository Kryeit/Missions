package com.kryeit.screen;

import com.kryeit.missions.ActiveMission;
import com.kryeit.missions.wrappers.Player;
import com.kryeit.screen.button.MissionButton;
import com.kryeit.screen.button.RewardsButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

import static com.kryeit.missions.MissionManager.getActiveMissions;

public class MissionScreen extends Screen {
    private Button myButton;
    private Button rewardButton;

    public MissionScreen() {
        super(new TextComponent("Mission GUI"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 5; // Space between buttons

        int leftX = (this.width / 2 - buttonWidth - spacing);
        int rightX = (this.width / 2 + spacing);

        // Get the active missions
        List<ActiveMission> activeMissions = getActiveMissions(Minecraft.getInstance().player.getUUID());

        // Calculate the number of missions for each column
        int missionsPerColumn = (int) Math.ceil((double) activeMissions.size() / 2);

        for (int i = 0; i < missionsPerColumn; i++) {
            int y = (this.height - (missionsPerColumn * buttonHeight + (missionsPerColumn - 1) * spacing)) / 2 + i * (buttonHeight + spacing);

            // Use the mission's item as the button's title
            ActiveMission leftColumnMission = activeMissions.get(i);
            TranslatableComponent leftColumnTitle = new TranslatableComponent(leftColumnMission.item());

            // Left column
            this.myButton = this.addRenderableWidget(new MissionButton(leftX, y, buttonWidth, buttonHeight, leftColumnTitle, button -> {
                // Button clicked
            }));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                TranslatableComponent rightColumnTitle = new TranslatableComponent(rightColumnMission.item());

                // Right column
                this.myButton = this.addRenderableWidget(new MissionButton(rightX, y, buttonWidth, buttonHeight, rightColumnTitle, button -> {
                    // Button clicked
                }));
            }
        }


        closeButton();
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
        int centerY = this.height - buttonHeight - bottomPadding; // 10 pixels from the bottom

        this.addRenderableWidget(new Button(centerX, centerY, buttonWidth, buttonHeight, new TranslatableComponent("key.mission_gui.close"), button -> {
            Minecraft.getInstance().setScreen(null);
        }));
    }

    public void rewardButton() {
// Add the new button at the bottom right
        int buttonSize = 20;
        int rightPadding = 50;
        int bottomPadding = 20;
        int x = this.width - buttonSize - rightPadding;
        int y = this.height - buttonSize - bottomPadding;

        this.rewardButton = this.addRenderableWidget(new RewardsButton(x, y, buttonSize, buttonSize, new TextComponent(""), button -> {
            // Button clicked
        }));
    }
}
