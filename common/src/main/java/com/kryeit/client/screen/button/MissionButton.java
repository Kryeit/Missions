package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MissionButton extends Button {
    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/mission_button.png");
    private static final Font font = Minecraft.getInstance().font;
    private final boolean completed;
    private final ItemStack item;

    public MissionButton(int x, int y, int width, int height, Component message, boolean completed, ItemStack item, OnPress onPress) {
        super(x, y, width, height, message, onPress);
        this.completed = completed;
        this.item = item;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderButtonTexture(matrices);
        renderItem();
        int color = completed ? 0x00FF00 : 0xFFFFFF;
        AbstractWidget.drawCenteredString(matrices, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
    }

    public void renderButtonTexture(PoseStack matrices) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(BUTTON_TEXTURE);

        int textureWidth = 200;
        int textureHeight = 20;

        int x = this.x + (this.width - textureWidth) / 2;
        int y = this.y + (this.height - textureHeight) / 2;

        RenderSystem.setShaderTexture(0, BUTTON_TEXTURE);
        blit(matrices, x, y, 0, 0, textureWidth, textureHeight);
    }

    public void renderItem() {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int textureX = x + width / 2 - 94;
        int textureY = y + height / 2 - 8; // Center of the button, minus half the size of the texture
        itemRenderer.renderGuiItem(item, textureX, textureY);
    }
}
