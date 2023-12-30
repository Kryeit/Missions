package com.kryeit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Main.init();
        Main.registrate().register();
        MissionHandler.registerEvents();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Main.handlePlayerLogin(handler.getPlayer()));
    }
}
