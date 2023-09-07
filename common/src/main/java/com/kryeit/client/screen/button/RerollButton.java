package com.kryeit.client.screen.button;

import com.kryeit.client.ClientsideMissionPacketUtils;
import com.kryeit.client.screen.MissionRerollScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.kryeit.client.screen.button.MissionButton.ADVANCEMENT_WIDGETS;


public class RerollButton extends Button {
    private static final OnPress NO_PRESS = button -> { };
    private static final Component REROLL = new TextComponent("    ").append(new TranslatableComponent("missions.menu.reroll.reroll"));
    private final int missionIndex;
    private final ItemStack rerollPrice;


    public RerollButton(int x, int y, int sizeX, int sizeY, int missionIndex, ItemStack rerollPrice) {
        super(x, y, sizeX, sizeY, REROLL, NO_PRESS);
        this.missionIndex = missionIndex;
        this.rerollPrice = rerollPrice;
    }

    @Override
    public void onPress() {
        ClientsideMissionPacketUtils.requestReroll(missionIndex);
        MissionRerollScreen.close();
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        renderItem(matrices);
    }

    public void renderItem(PoseStack matrices) {
        renderBelowItem(matrices);

        ResourceLocation item = Registry.ITEM.getKey(rerollPrice.getItem());
        ResourceLocation textureLocation = new ResourceLocation(item.getNamespace(), "textures/item/" + item.getPath() + ".png");
        RenderSystem.setShaderTexture(0, textureLocation);

        int textureX = x + width / 2 - 42;
        int textureY = y + height / 2 - 8;

        blit(matrices, textureX, textureY, 0, 0, 16, 16, 16, 16);

        // Render the item stack's amount
        String amountText = rerollPrice.getCount() > 1 ? String.valueOf(rerollPrice.getCount()) : "";
        Font fontRenderer = Minecraft.getInstance().font;
        int textX = textureX + 17 - fontRenderer.width(amountText);
        int textY = textureY + 9;
        fontRenderer.drawShadow(matrices, amountText, textX, textY, 0xFFFFFF);
    }


    public void renderBelowItem(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(ADVANCEMENT_WIDGETS);

        int v = 154;

        if (isHovered) v -= 26;

        int u = 26;

        int textureSize = 26;

        int x = this.x + 3;
        int y = this.y - 3;

        RenderSystem.setShaderTexture(0, ADVANCEMENT_WIDGETS);
        blit(matrices, x, y, u, v, textureSize, textureSize, 256, 256);
    }
}
