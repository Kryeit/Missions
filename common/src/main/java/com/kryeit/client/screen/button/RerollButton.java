package com.kryeit.client.screen.button;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.MissionRerollScreen;
import com.kryeit.missions.MissionDifficulty;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.model.BakedModel;
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
        renderBelowItem(guiGraphics);

        BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getModel(rerollPrice, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
        ResourceLocation auxLocation = bakedModel.getParticleIcon().contents().name();
        String namespace = auxLocation.getNamespace();
        String path = auxLocation.getPath();

        ResourceLocation textureLocation = new ResourceLocation(namespace, "textures/" + path + ".png");

        System.out.println(textureLocation);
        if (rerollPrice.getCount() == 1)
            textureLocation = RewardsButton.CHEST_TEXTURE;

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
        int v = 154;

        if (isHovered) v -= 26;

        int u = switch (difficulty) {
            case NORMAL -> 26 * 2;
            case HARD -> 26;
            default -> 0;
        };

        int textureSize = 26;

        int x = this.getX() + 3;
        int y = this.getY() - 3;

        guiGraphics.blit(ADVANCEMENT_WIDGETS, x, y, u, v, textureSize, textureSize, 256, 256);
    }
}
