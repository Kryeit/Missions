package com.kryeit.forge.screen;

import com.kryeit.screen.button.RewardsButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class TestScreen extends Screen {

    private Button button;
    private Button rewardsButton;

    public TestScreen() {
        super(new TextComponent("My Screen"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.button = this.addRenderableWidget(new Button(centerX - 50, centerY - 10, 100, 20, new TextComponent("Click Me"), (button) -> {
            // Add your button click logic here
        }));
        setRewardsButton();

    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderBackground(matrixStack);
        this.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.button.render(matrixStack, mouseX, mouseY, partialTicks);
        this.rewardsButton.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void setRewardsButton() {
        int buttonSize = 20;
        int rightPadding = 50;
        int bottomPadding = 20;
        int x = this.width - buttonSize - rightPadding;
        int y = this.height - buttonSize - bottomPadding;

        this.rewardsButton = this.addRenderableWidget(new RewardsButton(x, y, buttonSize, buttonSize, new TextComponent(""),(button) -> {

        }));

    }
}

