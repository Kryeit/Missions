package com.kryeit.fabric.screen;


import com.kryeit.Main;
import com.kryeit.fabric.screen.button.MissionButton;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import static com.kryeit.fabric.client.ClientEntryPoint.missionGuiKey;
import static com.kryeit.fabric.screen.button.MissionButton.BUTTON_TEXTURE;

public class TestScreen extends Screen {
    private Button myButton;

    public TestScreen(TranslatableComponent title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 5; // Space between buttons

        int leftX = (this.width / 2 - buttonWidth - spacing);
        int rightX = (this.width / 2 + spacing);

        for(int i = 0; i < 5; i++) {
            int y = (this.height - (5 * buttonHeight + 4 * spacing)) / 2 + i * (buttonHeight + spacing);

            this.myButton = this.addRenderableWidget(new MissionButton(leftX, y, buttonWidth, buttonHeight, new TranslatableComponent("test"), button -> {
                // Button clicked
            }));

            this.myButton = this.addRenderableWidget(new MissionButton(rightX, y, buttonWidth, buttonHeight, new TranslatableComponent("test"), button -> {
                // Button clicked
            }));
        }
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
}

