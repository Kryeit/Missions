package com.kryeit.client.screen;

import com.kryeit.client.screen.button.RerollButton;
import com.kryeit.missions.MissionDifficulty;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static com.kryeit.client.screen.MissionScreen.CLOSE;

public class MissionRerollScreen extends Screen {
    private static final Component TITLE = Components.translatable("missions.menu.reroll.title");
    private final int missionIndex;
    private final ItemStack rerollPrice;
    private final MissionDifficulty difficulty;

    public MissionRerollScreen(int missionIndex, ItemStack rerollPrice, MissionDifficulty difficulty) {
        super(TITLE);
        this.missionIndex = missionIndex;
        this.rerollPrice = rerollPrice;
        this.difficulty = difficulty;
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

        this.addRenderableWidget(new RerollButton(startX + buttonWidth + 5, centerY, buttonWidth, buttonHeight, missionIndex, rerollPrice, difficulty));
    }

    public static void close() {
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
