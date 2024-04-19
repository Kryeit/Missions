package com.kryeit.client.screen.button;

import com.kryeit.registry.ModStats;
import com.kryeit.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoButton extends Button {

    public static final ResourceLocation INFO_ICON = new ResourceLocation("textures/gui/info_icon.png");
    private static final OnPress ON_PRESS = button -> {};

    public InfoButton(int x, int y) {
        super(x, y, 20, 20, Component.empty(), ON_PRESS, Button.DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int textureSize = 20;
        guiGraphics.blit(INFO_ICON, this.getX(), this.getY(), 0, 0, textureSize, textureSize, 20, 20);

        if (isHovered) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, getInfoTooltip(), Optional.empty(), mouseX, mouseY);
        }
    }

    public static List<Component> getInfoTooltip() {
        List<Component> components = new ArrayList<>();

        components.add(Component.literal("Stats:").withStyle(ChatFormatting.AQUA));
        components.add(Component.literal("Missions rerolled: " + Utils.getClientStat(ModStats.MISSIONS_REROLLED)).withStyle(ChatFormatting.AQUA));
        components.add(Component.literal("Total missions completed: " + Utils.getTotalMissions()).withStyle(ChatFormatting.AQUA));
        components.add(Component.literal("- Easy: " + Utils.getClientStat(ModStats.EASY_MISSIONS_COMPLETED)).withStyle(ChatFormatting.AQUA));
        components.add(Component.literal("- Normal: " + Utils.getClientStat(ModStats.NORMAL_MISSIONS_COMPLETED)).withStyle(ChatFormatting.AQUA));
        components.add(Component.literal("- Hard: " + Utils.getClientStat(ModStats.HARD_MISSIONS_COMPLETED)).withStyle(ChatFormatting.AQUA));

        return components;
    }
}
