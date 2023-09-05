package com.kryeit.client.screen;

import com.kryeit.client.screen.button.RerollButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import static com.kryeit.client.screen.MissionScreen.CLOSE;

public class MissionRerollScreen extends Screen {
    private static final Component TITLE = new TranslatableComponent("missions.menu.reroll.title");
    private final int missionIndex;
    private final ItemStack rerollPrice;

    public MissionRerollScreen(int missionIndex, ItemStack rerollPrice) {
        super(TITLE);
        this.missionIndex = missionIndex;
        this.rerollPrice = rerollPrice;
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

        this.addRenderableWidget(new RerollButton(startX + buttonWidth + 5, centerY, buttonWidth, buttonHeight, missionIndex, rerollPrice));
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
