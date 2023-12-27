package com.kryeit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Main.init();
        Main.registrate().register();
        MissionHandler.registerEvents();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("missions")
                    .executes(context -> {
                        // This is made by ChatScreenMixin.java
                        //context.getSource().getClient().setScreen(new MissionScreen());
                        return 1;
                    }));
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Main.handlePlayerLogin(handler.getPlayer()));
    }
}
