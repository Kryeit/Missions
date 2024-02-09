package com.kryeit.client.screen.button;

import com.kryeit.Main;
import com.kryeit.client.ClientsideMissionPacketUtils;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class RewardsButton extends Button {
    private static final Component REWARDS = Components.literal("    ").append(Components.translatable("missions.menu.main.rewards"));
    public static final ResourceLocation CHEST_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/christmas_chest.png");
    private static final ResourceLocation OPEN_CHEST_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/open_christmas_chest.png");
    private boolean rewardsAvailable;

    public RewardsButton(int x, int y, final boolean rewardsAvailable) {
        super(x, y, 100, 20, REWARDS, button -> {}, DEFAULT_NARRATION);

        this.rewardsAvailable = rewardsAvailable;
    }

    @Override
    public void onPress() {
        ClientsideMissionPacketUtils.requestPayout();
        rewardsAvailable = false;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);

        int textureX = getX() + width / 2 - 46;
        int textureY = getY() + height / 2 - 19;
        guiGraphics.blit(rewardsAvailable ? CHEST_TEXTURE : OPEN_CHEST_TEXTURE, textureX, textureY, 21, 28, 35, 3, 185, 250, 256, 256);

        if (isHovered && !rewardsAvailable) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font,
                    List.of(Component.translatable("missions.menu.main.rewards.tooltip.empty").withStyle(ChatFormatting.DARK_GRAY)),
                    Optional.empty(), mouseX, mouseY);
        }
    }
}
