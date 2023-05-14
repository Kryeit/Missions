package com.kryeit.fabric.screen;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

import static com.kryeit.fabric.client.ClientEntryPoint.missionGuiKey;

public class TestScreen extends Screen {
    private Button myButton;

    public TestScreen(TranslatableComponent title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        int x = (this.width - 200) / 2;
        int y = (this.height - 20) / 2;

        this.myButton = this.addWidget(new Button(x, y, 200, 20, new TranslatableComponent("test"), button -> {
            // Button clicked
            Minecraft.getInstance().player.sendMessage(new TranslatableComponent("test"), Util.NIL_UUID);
        }));
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

