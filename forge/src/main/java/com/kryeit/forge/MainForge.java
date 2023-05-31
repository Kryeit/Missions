package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.client.KeyInit;
import com.kryeit.missions.MissionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@Mod(Main.MOD_ID)
public class MainForge {

    public MainForge() {
        Main.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(new MissionHandler());
        eventBus.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) event -> {
            boolean reassigned = MissionManager.reassignMissionsIfNecessary(event.getPlayer().getUUID());
            if (reassigned) {
                // send a message, I don't know?
            }
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        KeyInit.init();
    }
}
