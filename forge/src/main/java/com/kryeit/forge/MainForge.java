package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.init.ModBlocks;
import com.kryeit.forge.init.ModBlockEntities;
import com.kryeit.forge.init.KeyInit;
import com.kryeit.forge.init.ModItems;
import com.kryeit.forge.init.ModRecipes;
import com.kryeit.forge.init.ModMenuTypes;
import com.kryeit.forge.tab.CreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class MainForge {

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Main.MOD_ID);


    public MainForge() {


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);
        CreativeModeTabs.init();
        ModBlocks.register();
        ModItems.register();
        ModMenuTypes.register();
        ModBlockEntities.register();
        ModRecipes.register(modEventBus);

        Main.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        forgeEventBus.register(new MissionHandler());
        //TODO: THIS DOESNT WORK, THE LISTENER IS WRONG ARGUMENT OR SOMETHING
        //eventBus.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) event -> {
        //    boolean reassigned = MissionManager.reassignMissionsIfNecessary(event.getPlayer().getUUID());
        //    if (reassigned) {
        //        // send a message, I don't know?
        //    }
        //});
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        KeyInit.init();
    }
}
