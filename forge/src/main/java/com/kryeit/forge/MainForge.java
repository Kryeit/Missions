package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.block.ModBlocks;
import com.kryeit.forge.block.entity.ModBlockEntities;
import com.kryeit.forge.client.KeyInit;
import com.kryeit.forge.item.ModItems;
import com.kryeit.forge.screen.ModMenuTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@Mod(Main.MOD_ID)
public class MainForge {

    public MainForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModMenuTypes.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModItems.register(eventBus);
        Main.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

//        eventBus.register(new MissionHandler());
//        eventBus.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) event -> {
//            boolean reassigned = MissionManager.reassignMissionsIfNecessary(event.getPlayer().getUUID());
//            if (reassigned) {
//                // send a message, I don't know?
//            }
//        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        KeyInit.init();
    }
}
