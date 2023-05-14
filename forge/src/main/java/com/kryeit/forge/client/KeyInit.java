package com.kryeit.forge.client;

import com.kryeit.Main;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class KeyInit {
    public static KeyMapping missionGuiKey;

    private KeyInit() {
    }

    public static void init() {
        missionGuiKey = registerKey("mission_gui", "key.category.missions", InputConstants.KEY_H);
    }

    private static KeyMapping registerKey(String name, String category, int keycode) {
        final var key = new KeyMapping("key." + Main.MOD_ID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (missionGuiKey.isDown()) {
            Minecraft.getInstance().player.sendMessage(new TranslatableComponent("test"), Minecraft.getInstance().player.getUUID());
        }
    }


}

