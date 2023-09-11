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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionScreen extends Screen {
    private static final Component TITLE = new TranslatableComponent("missions.menu.main.title");
    public static final TranslatableComponent CLOSE = new TranslatableComponent("missions.menu.close");
    private final Runnable NO_TOOLTIP = () -> {
    };
    public Runnable activeTooltip = NO_TOOLTIP;
    private ClientMissionData data = null;

    public MissionScreen() {
        super(TITLE);
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
            this.addRenderableWidget(createMissionButton(leftX, y, leftColumnTitle, leftColumnMission, i, data.rerollPrice()));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ClientsideActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                Component rightColumnTitle = rightColumnMission.titleString();

                // Right column
                this.addRenderableWidget(createMissionButton(rightX, y, rightColumnTitle, rightColumnMission, i + missionsPerColumn, data.rerollPrice()));
            }
        }

        createInfoButton();
        createRewardButton(data.hasUnclaimedRewards());
    }

    private MissionButton createMissionButton(int x, int y, Component title, ClientsideActiveMission mission, int index, ItemStack rerollPrice) {
        Button.OnTooltip tooltip = (button, poseStack, mouseX, mouseY) -> renderTooltip(poseStack, getTooltip(mission), Optional.empty(), mouseX, mouseY);
        return new MissionButton(this, x, y, title, mission, tooltip, button -> {
            if(!mission.isCompleted() || rerollPrice.getItem() == Items.AIR) Minecraft.getInstance().setScreen(new MissionRerollScreen(index, rerollPrice));
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
        Component progress = mission.isCompleted()
                ? new TranslatableComponent("missions.menu.main.tooltip.progress.completed")
                : new TextComponent(mission.progress() + "/" + mission.requiredAmount());

        List<Component> components = new ArrayList<>();
        components.add(new TranslatableComponent("missions.menu.main.tooltip.details")
                .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));

        components.add(new TranslatableComponent("missions.menu.main.tooltip.task", mission.missionString().getString())
                .withStyle(ChatFormatting.WHITE));

        components.add(new TranslatableComponent("missions.menu.main.tooltip.difficulty", mission.difficulty().description())
                .withStyle(ChatFormatting.BLUE));

        ItemStack itemRequired = mission.itemRequired();
        if (!itemRequired.is(Items.AIR)) {
            components.add(new TranslatableComponent("missions.menu.main.tooltip.itemRequired", itemRequired.getDisplayName().getString())
                    .withStyle(ChatFormatting.BLUE));
        }

        components.add(new TranslatableComponent("missions.menu.main.tooltip.progress", progress)
                .withStyle(ChatFormatting.GREEN));

        return components;
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

        this.addRenderableWidget(new Button(x, y, buttonWidth, buttonHeight, CLOSE, button -> Minecraft.getInstance().setScreen(null)));
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
        int x = this.width / 2 + spacing;
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new RewardsButton(x, y, rewardsAvailable));
    }
}
