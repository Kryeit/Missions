package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
public class RewardsButton extends Button {
    private static final Component REWARDS = Components.literal("    ").append(Components.translatable("missions.menu.main.rewards"));
    private static final OnPress NO_PRESS = button -> {
    };
    public static final ResourceLocation CHEST_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/christmas_chest.png");
    private static final ResourceLocation OPEN_CHEST_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/open_christmas_chest.png");
    private boolean rewardsAvailable;

    public RewardsButton(int x, int y, boolean rewardsAvailable) {
        super(x, y, 100, 20, REWARDS, NO_PRESS);
        this.rewardsAvailable = rewardsAvailable;
    }

    @Override
    public void onPress() {
        ClientsideMissionPacketUtils.requestPayout();
        rewardsAvailable = false;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);

        RenderSystem.setShaderTexture(0, rewardsAvailable ? CHEST_TEXTURE : OPEN_CHEST_TEXTURE);

        int textureX = x + width / 2 - 46;
        int textureY = y + height / 2 - 19;
        blit(matrices, textureX, textureY, 21, 28, 35, 3, 185, 250, 256, 256);
    }
}
