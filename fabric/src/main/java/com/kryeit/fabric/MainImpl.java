package com.kryeit.fabric;

import com.kryeit.Main;
import com.kryeit.MinecraftServerSupplier;
import com.kryeit.MissionHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static com.kryeit.Main.REGISTRATE;

public class MainImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        Main.init();
        MissionHandler.registerEvents();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Main.handlePlayerLogin(handler.getPlayer()));

        if (MinecraftServerSupplier.getServer() == null) {
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



    public static void finalizeRegistrate() {
        REGISTRATE.register();
    }


}
