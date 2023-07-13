package com.kryeit.forge.init;

import com.kryeit.forge.tab.CreativeModeTabs;

import static com.kryeit.forge.MainForge.REGISTRATE;


public class ModItems {
    static {
        REGISTRATE.creativeModeTab(() -> CreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static void register() {}
}
