package com.kryeit.forge;

import com.kryeit.Main;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@Mod(Main.MOD_ID)
public class MainForge {
    public MainForge() {
        Main.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Main.registrate().registerEventListeners(modEventBus);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(new MissionHandler());
        forgeEventBus.addListener((Consumer<PlayerLoggedInEvent>) event -> Main.handlePlayerLogin(event.getEntity()));
    }

}
