package com.kryeit.client.screen;

import com.kryeit.Missions;
import com.kryeit.client.ClientMissionData;
import com.kryeit.client.ClientMissionData.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.button.InfoButton;
import com.kryeit.client.screen.button.MissionButton;
import com.kryeit.client.screen.button.RewardsButton;
import com.kryeit.missions.mission_types.create.train.TrainDriverPassengerMission;
import com.kryeit.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissionScreen extends Screen {
    public static final ResourceLocation MISSIONS_TITLE = Missions.asResource("textures/gui/title.png");
    public static final Component CLOSE = Component.translatable("missions.menu.close");
    private ClientMissionData data = null;

    public MissionScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    protected void init() {
        super.init();

        ClientsideMissionPacketUtils.setMissionUpdateHandler(missionData -> data = missionData);
        ClientsideMissionPacketUtils.requestMissions();

        createCloseButton();
    }

    private void addMissions(ClientMissionData data) {
        int buttonHeight = 20;
        int rowSpacing = 5;
        int margin = 10;
        int colGap = Math.max(6, this.width / 40);

        // Adaptive button width: caps at a comfortable 160 (so it is not huge) but shrinks to fit
        // narrow screens, and the two-column group is centered, so it respects the GUI scale setting.
        int avail = this.width - 2 * margin - colGap;
        int buttonWidth = Math.max(80, Math.min(160, avail / 2));
        int groupWidth = 2 * buttonWidth + colGap;
        int leftX = Math.max(margin, (this.width - groupWidth) / 2);
        int rightX = leftX + buttonWidth + colGap;

        List<ClientsideActiveMission> activeMissions = data.activeMissions();

        if (activeMissions.size() != 10) {
            Minecraft.getInstance().gui.getChat().addMessage(Component.translatable("Something wrong happened, you don't have 10 missions. Contact an admin"));
            return;
        }

        int missionsPerColumn = 5;
        int totalH = missionsPerColumn * buttonHeight + (missionsPerColumn - 1) * rowSpacing;

        for (int i = 0; i < missionsPerColumn; i++) {
            int y = (this.height - totalH) / 2 + i * (buttonHeight + rowSpacing);

            // Use the mission's item as the button's title
            ClientsideActiveMission leftColumnMission = activeMissions.get(i);
            Component leftColumnTitle = leftColumnMission.titleString();

            // Left column
            this.addRenderableWidget(createMissionButton(leftX, y, buttonWidth, leftColumnTitle, leftColumnMission, i, data.rerollPrice()));

            if (i + missionsPerColumn < activeMissions.size()) {
                // There's a mission for the right column
                ClientsideActiveMission rightColumnMission = activeMissions.get(i + missionsPerColumn);
                Component rightColumnTitle = rightColumnMission.titleString();

                // Right column
                this.addRenderableWidget(createMissionButton(rightX, y, buttonWidth, rightColumnTitle, rightColumnMission, i + missionsPerColumn, data.rerollPrice()));
            }
        }

        createInfoButton(data);
        createRewardButton(data.hasUnclaimedRewards());
    }

    private MissionButton createMissionButton(int x, int y, int width, Component title, ClientsideActiveMission mission, int index, ItemStack rerollPrice) {
        return new MissionButton(x, y, width, title, mission, button -> {
            if(!mission.isCompleted() && rerollPrice.getItem() != Items.AIR) {
                Minecraft.getInstance().setScreen(new MissionRerollScreen(index, rerollPrice, mission.difficulty()));
            }
        });
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        if (data != null) {
            addMissions(data);
            data = null;
        }

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 40, 0xFFFFFF);
        renderTitle(guiGraphics);
    }

    public void renderTitle(GuiGraphics guiGraphics) {
        int margin = 10;
        int titleW = Math.min(200, this.width - 2 * margin);
        int titleH = titleW * 44 / 200; // preserve the 200x44 aspect
        int titleX = (this.width - titleW) / 2;
        int titleY = this.height / 35;
        guiGraphics.blit(MISSIONS_TITLE, titleX, titleY, titleW, titleH, 0, 0, 256, 56, 256, 256);
    }

    public static List<Component> getTooltip(ClientsideActiveMission mission) {

        // mission.requiredAmount formatted so thousands are like 1,000
        NumberFormat numberFormat = NumberFormat.getInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        ((DecimalFormat) numberFormat).setDecimalFormatSymbols(symbols);
        String requiredAmount = numberFormat.format(mission.requiredAmount());

        Component progress = mission.isCompleted()
                ? Component.translatable("missions.menu.main.tooltip.progress.completed")
                : Component.translatable(numberFormat.format(mission.progress()) + "/" + requiredAmount);

        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("missions.menu.main.tooltip.details")
                .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));

        String itemName = Utils.removeBrackets(mission.itemRequired().getDisplayName().getString());

        if (Items.AIR == mission.itemRequired().getItem()) {

            if (Objects.equals(mission.missionType(), "train-driver-passenger")) {
                components.add(
                        Utils.getMissionMessage(mission,
                                ChatFormatting.WHITE, requiredAmount, TrainDriverPassengerMission.passengersNeeded())
                );
            } else {
                components.add(
                        Utils.getMissionMessage(mission,
                                ChatFormatting.WHITE, requiredAmount)
                );
            }

        } else if (mission.itemRequired().getItem() instanceof SpawnEggItem) {
            // This cannot be backported, 1.20+ contains a spawn egg for every mob
            components.add(
                    Utils.getMissionMessage(mission,
                            ChatFormatting.WHITE, requiredAmount, Utils.getEntityOfSpawnEggForTooltip(mission.itemRequired()))
            );
        } else if (mission.itemRequired().getItem() instanceof BucketItem) {
            components.add(
                    Utils.getMissionMessage(mission,
                            ChatFormatting.WHITE, Utils.getFluidFromBucketForTooltip(mission.itemRequired()), requiredAmount)
            );
        } else {
            components.add(
                    Utils.getMissionMessage(mission,
                            ChatFormatting.WHITE, requiredAmount, itemName)
            );
        }

        if (!mission.isCompleted())
            components.add(Component.translatable("missions.menu.main.tooltip.reward", mission.rewardText(),
                            Utils.removeBrackets(BuiltInRegistries.ITEM.get(ResourceLocation.parse(mission.rewardItemLocation())).getDefaultInstance().getDisplayName().getString()))
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

        components.add(Component.translatable("missions.menu.main.tooltip.progress", progress)
                .withStyle(ChatFormatting.GREEN));

        if (!mission.isCompleted())
            components.add(Component.translatable("missions.menu.main.tooltip.click")
                    .withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));

        return components;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /** Width of the close/rewards buttons flanking the centered info button (shrinks on narrow screens). */
    private int bottomSideButtonWidth() {
        int margin = 10, gap = 6, infoW = 20;
        return Math.max(70, Math.min(100, (this.width - 2 * margin - infoW - 2 * gap) / 2));
    }

    public void createCloseButton() {
        int gap = 6;
        int infoW = 20;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int sideBtnW = bottomSideButtonWidth();
        int x = this.width / 2 - infoW / 2 - gap - sideBtnW;
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(Button.builder(CLOSE, button -> Minecraft.getInstance().setScreen(null))
                .bounds(x, y, sideBtnW, buttonHeight)
                .build());
    }

    public void createInfoButton(ClientMissionData data) {
        int buttonWidth = 20;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int x = this.width / 2 - buttonWidth / 2;
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new InfoButton(x, y, this, data));
    }

    public void createRewardButton(boolean rewardsAvailable) {
        int gap = 6;
        int infoW = 20;
        int buttonHeight = 20;
        int bottomPadding = 20;
        int sideBtnW = bottomSideButtonWidth();
        int x = this.width / 2 + infoW / 2 + gap;
        int y = this.height - buttonHeight - bottomPadding;

        this.addRenderableWidget(new RewardsButton(x, y, sideBtnW, rewardsAvailable));
    }
}