package com.kryeit.client.screen.button;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.MissionRerollScreen;
import com.kryeit.missions.MissionDifficulty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class RerollButton extends Button {
    private static final OnPress NO_PRESS = button -> { };
    private static final Component REROLL = Component.literal("    ").append(Component.translatable("missions.menu.reroll.reroll"));
    private final int missionIndex;
    private final ItemStack rerollPrice;

    private final MissionDifficulty difficulty;

    public RerollButton(int x, int y, int sizeX, int sizeY, int missionIndex, ItemStack rerollPrice, MissionDifficulty difficulty) {
        super(x, y, sizeX, sizeY, REROLL, NO_PRESS, Button.DEFAULT_NARRATION);
        this.missionIndex = missionIndex;
        this.rerollPrice = rerollPrice;
        this.difficulty = difficulty;
    }

    @Override
    public void onPress() {
        ClientsideMissionPacketUtils.requestReroll(missionIndex);
        MissionRerollScreen.close();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);
        renderItem(guiGraphics);
    }

    public void renderItem(GuiGraphics guiGraphics) {
        int x = getX() + width / 2 - 42;
        int y = getY() + height / 2 - 8;
        guiGraphics.renderItem(rerollPrice, x, y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, rerollPrice, x, y);
    }
}
