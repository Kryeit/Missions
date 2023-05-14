package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.client.KeyInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class MainForge {

    public MainForge() {
        Main.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }



    private void doClientStuff(final FMLClientSetupEvent event) {
        KeyInit.init();
    }


}
