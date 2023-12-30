package com.kryeit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class MainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager
                .literal("missions")
                .executes(context -> {
                    // This is made by ChatScreenMixin.java
                    //context.getSource().getClient().setScreen(new MissionScreen());
                    return 1;
                }));
    }
}
