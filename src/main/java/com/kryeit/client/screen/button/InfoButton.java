package com.kryeit.client.screen.button;

import com.kryeit.client.ClientMissionData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfoButton extends Button {

    public Screen screen;
    private final ClientMissionData data;

    public InfoButton(int x, int y, Screen screen, ClientMissionData data) {
        super(x, y, 20, 20, Component.literal("?"), button -> {}, Button.DEFAULT_NARRATION);
        this.screen = screen;
        this.data = data;
    }

    @Override
    public void onPress() {
        Minecraft.getInstance().setScreen(new StatsScreen(screen, Minecraft.getInstance().player.getStats()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);

        if (isHovered) {
            List<Component> tooltip = getInfoTooltip();
            int tooltipWidth = tooltip.stream()
                    .mapToInt(component -> Minecraft.getInstance().font.width(component.getString()))
                    .max()
                    .orElse(0);
            int tooltipX = this.getX() + (this.getWidth() / 2) - (tooltipWidth / 2);
            int tooltipY = this.getY() - Minecraft.getInstance().font.lineHeight * tooltip.size() - 5;
            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, Optional.empty(), tooltipX, tooltipY);
        }
    }

    public List<Component> getInfoTooltip() {
        int total = data.easyCompleted() + data.normalCompleted() + data.hardCompleted();

        List<Component> components = new ArrayList<>();
        components.add(Component.literal("Stats:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
        components.add(Component.literal("Missions rerolled: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(data.missionsRerolled())).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal(""));
        components.add(Component.literal("Total missions completed: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(total)).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
        components.add(Component.literal("- Easy: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(data.easyCompleted())).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("- Normal: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(data.normalCompleted())).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("- Hard: ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(data.hardCompleted())).withStyle(ChatFormatting.GREEN)));
        components.add(Component.literal("Click to open the Stats screen.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

        return components;
    }
}
