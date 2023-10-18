package com.kryeit.client.screen.button;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.MissionRerollScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.kryeit.client.screen.button.MissionButton.ADVANCEMENT_WIDGETS;


public class RerollButton extends Button {
    private static final OnPress NO_PRESS = button -> { };
    private static final Component REROLL = Components.literal("    ").append(Components.translatable("missions.menu.reroll.reroll"));
    private final int missionIndex;
    private final ItemStack rerollPrice;


    public RerollButton(int x, int y, int sizeX, int sizeY, int missionIndex, ItemStack rerollPrice) {
        super(x, y, sizeX, sizeY, REROLL, NO_PRESS, Button.DEFAULT_NARRATION);
        this.missionIndex = missionIndex;
        this.rerollPrice = rerollPrice;
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
        renderBelowItem(guiGraphics);

        ResourceLocation item = BuiltInRegistries.ITEM.getKey(rerollPrice.getItem());
        ResourceLocation textureLocation = new ResourceLocation(item.getNamespace(), "textures/item/" + item.getPath() + ".png");
        RenderSystem.setShaderTexture(0, textureLocation);

        int textureX = getX() + width / 2 - 42;
        int textureY = getY() + height / 2 - 8;

        guiGraphics.blit(textureLocation, textureX, textureY, 0, 0, 16, 16, 16, 16);

        // Render the item stack's amount
        String amountText = rerollPrice.getCount() > 1 ? String.valueOf(rerollPrice.getCount()) : "";
        Font fontRenderer = Minecraft.getInstance().font;
        int textX = textureX + 17 - fontRenderer.width(amountText);
        int textY = textureY + 9;
        guiGraphics.drawString(fontRenderer, amountText, textX, textY, 0xFFFFFF);
    }


    public void renderBelowItem(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int v = 154;

        if (isHovered) v -= 26;

        int u = 26;

        int textureSize = 26;

        int x = this.getX() + 3;
        int y = this.getY() - 3;

        RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        guiGraphics.blit(ADVANCEMENT_WIDGETS, x, y, u, v, textureSize, textureSize, 256, 256);
    }
}
