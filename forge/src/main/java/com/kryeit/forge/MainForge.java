package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.block.ModBlocks;
import com.kryeit.forge.block.entity.ModBlockEntities;
import com.kryeit.forge.client.KeyInit;
import com.kryeit.forge.item.ModItems;
import com.kryeit.forge.recipe.ModRecipes;
import com.kryeit.forge.screen.ModMenuTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
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
        ModBlocks.register();
        ModItems.register(forgeEventBus);
        ModMenuTypes.register();
        ModBlockEntities.register();
        ModRecipes.register(forgeEventBus);

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
