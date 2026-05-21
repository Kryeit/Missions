package com.kryeit.forge.events;

import com.kryeit.events.ClientEvents;
import com.kryeit.registry.forge.ModKeysImpl;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventsForge {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            ClientEvents.onClientTickStart(Minecraft.getInstance());
        else if (event.phase == TickEvent.Phase.END)
            ClientEvents.onClientTickEnd(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        int key = event.getKey();
        boolean pressed = event.getAction() != 0;
        ClientEvents.onKeyInput(key, pressed);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeysImpl.onRegisterKeyMappings(event);
        }
    }
}
