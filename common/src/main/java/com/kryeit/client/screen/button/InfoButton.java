package com.kryeit.client.screen.button;

import com.kryeit.client.screen.MissionScreen;
import com.kryeit.registry.ModStats;
import com.kryeit.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoButton extends Button {

    public Screen screen;

    public static final ResourceLocation INFO_ICON = new ResourceLocation("textures/gui/info_icon.png");

    public InfoButton(int x, int y, Screen screen) {
        super(x, y, 20, 20, Component.empty(), button -> {}, Button.DEFAULT_NARRATION);
        this.screen = screen;

    }

    @Override
    public void onPress() {
        Minecraft.getInstance().setScreen(new StatsScreen(screen, Minecraft.getInstance().player.getStats()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int textureSize = 20;
        guiGraphics.blit(INFO_ICON, this.getX(), this.getY(), 0, 0, textureSize, textureSize, 20, 20);

        if (isHovered) {
            int tooltipWidth = getInfoTooltip().stream()
                    .mapToInt(component -> Minecraft.getInstance().font.width(component.getString()))
                    .max()
                    .orElse(0);
            int tooltipX = this.getX() + (this.getWidth() / 2) - (tooltipWidth / 2);
            int tooltipY = this.getY() - Minecraft.getInstance().font.lineHeight * getInfoTooltip().size() - 5;
            guiGraphics.renderTooltip(Minecraft.getInstance().font, getInfoTooltip(), Optional.empty(), tooltipX, tooltipY);
        }
    }

    public static List<Component> getInfoTooltip() {
        List<Component> components = new ArrayList<>();

        components.add(Component.literal("Stats:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
        components.add(Component.literal("Missions rerolled: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(Utils.getClientStat(ModStats.MISSIONS_REROLLED))).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal(""));
        components.add(Component.literal("Total missions completed: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(Utils.getTotalMissions())).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
        components.add(Component.literal("- Easy: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(Utils.getClientStat(ModStats.EASY_MISSIONS_COMPLETED))).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("- Normal: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(Utils.getClientStat(ModStats.NORMAL_MISSIONS_COMPLETED))).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("- Hard: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(Utils.getClientStat(ModStats.HARD_MISSIONS_COMPLETED))).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("Click to open the Stats screen.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

        return components;
    }
}