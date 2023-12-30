package com.kryeit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
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
