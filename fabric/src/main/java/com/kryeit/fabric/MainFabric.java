package com.kryeit.fabric;

import com.kryeit.Main;
import net.fabricmc.api.ModInitializer;

public class MainFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Main.init();
    }
}
