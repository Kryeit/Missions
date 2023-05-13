package com.kryeit.forge;

import com.kryeit.Main;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class MainForge {
    public MainForge() {
        // Submit our event bus to let architectury register our content on the right time
        Main.init();
    }
}
