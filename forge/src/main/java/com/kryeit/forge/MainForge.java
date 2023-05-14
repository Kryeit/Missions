package com.kryeit.forge;

import com.kryeit.Main;
import com.kryeit.forge.client.KeyInit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.kryeit.forge.client.KeyInit.missionGuiKey;

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
