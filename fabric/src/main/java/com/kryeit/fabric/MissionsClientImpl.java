package com.kryeit.fabric;

import com.kryeit.Missions;
import com.kryeit.MissionsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;


public class MissionsClientImpl implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        MissionsClient.initializeClient();
        ClientEventsFabric.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(server ->
                Missions.readConfig()
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager
                    .literal("missions")
                    .executes(context -> {
                        // This is made by ChatScreenMixin.java
                        //context.getSource().getClient().setScreen(new MissionScreen());
                        return 1;
                    }));
        });
    }
}
