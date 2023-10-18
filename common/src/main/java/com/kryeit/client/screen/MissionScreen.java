package com.kryeit.client.screen;

import com.kryeit.MinecraftServerSupplier;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.button.InfoButton;
import com.kryeit.client.screen.button.MissionButton;
import com.kryeit.client.screen.button.RewardsButton;
import com.kryeit.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionScreen extends Screen {
    private static final Component TITLE = Components.translatable("missions.menu.main.title");
    public static final Component CLOSE = Components.translatable("missions.menu.close");
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
            Minecraft.getInstance().gui.getChat().addMessage(Components.translatable("Something wrong happened, you don't have 10 missions. Contact an admin"));
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
        return new MissionButton(x, y, title, mission, button -> {
            if(!mission.isCompleted() && rerollPrice.getItem() != Items.AIR && (MinecraftServerSupplier.getServer() == null || MinecraftServerSupplier.getServer().isSingleplayer())) Minecraft.getInstance().setScreen(new MissionRerollScreen(index, rerollPrice));
        });
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, delta);
        if (data != null) {
            addMissions(data);
            data = null;
        }

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 40, 0xFFFFFF);
    }

    public static List<Component> getTooltip(ClientsideActiveMission mission) {
        Component progress = mission.isCompleted()
                ? Components.translatable("missions.menu.main.tooltip.progress.completed")
                : Components.translatable(mission.progress() + "/" + mission.requiredAmount());

        List<Component> components = new ArrayList<>();
        components.add(Components.translatable("missions.menu.main.tooltip.details")
                .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));


        components.add(Components.translatable("missions.menu.main.tooltip.task", mission.missionString().getString())
                .withStyle(ChatFormatting.WHITE));

        components.add(Components.translatable("missions.menu.main.tooltip.difficulty", mission.difficulty().description())
                .withStyle(ChatFormatting.BLUE));

        ItemStack itemRequired = mission.itemRequired();
        if (!itemRequired.is(Items.AIR))
            components.add(Components.translatable("missions.menu.main.tooltip.itemRequired", itemRequired.getDisplayName().getString())
                    .withStyle(ChatFormatting.BLUE));

        components.add(Components.translatable("missions.menu.main.tooltip.reward", mission.rewardAmount().lower() + "-" + mission.rewardAmount().upper(),
                Utils.removeBrackets(BuiltInRegistries.ITEM.get(new ResourceLocation(mission.rewardItemLocation())).getDefaultInstance().getDisplayName().getString()))
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        components.add(Components.translatable("missions.menu.main.tooltip.progress", progress)
                .withStyle(ChatFormatting.GREEN));

        return components;
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void createCloseButton() {
        int spacing = 5;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = (this.width / 2 - buttonWidth - spacing);
        int y = this.height - buttonHeight - bottomPadding;


        this.addRenderableWidget(Button.builder(CLOSE, button -> Minecraft.getInstance().setScreen(null))
                .bounds(x, y, buttonWidth, buttonHeight)
                .build());
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
