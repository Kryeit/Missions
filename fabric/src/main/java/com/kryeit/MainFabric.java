package com.kryeit;

import net.fabricmc.api.ModInitializer;

public class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Main.init();
        Main.registrate().register();
    }
}
