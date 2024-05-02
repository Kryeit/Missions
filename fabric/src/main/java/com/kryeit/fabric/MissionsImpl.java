package com.kryeit.fabric;

import com.kryeit.MissionHandler;
import com.kryeit.Missions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static com.kryeit.Missions.REGISTRATE;

public class MissionsImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        Missions.init();
        MissionHandler.registerEvents();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Missions.handlePlayerLogin(handler.getPlayer()));

        ServerLifecycleEvents.SERVER_STARTED.register(server ->
                Missions.readConfig()
        );
    }

    public static void finalizeRegistrate() {
        REGISTRATE.register();
    }

}
