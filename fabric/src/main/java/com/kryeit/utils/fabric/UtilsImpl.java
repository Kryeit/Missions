package com.kryeit.utils.fabric;

import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.fabricmc.loader.api.FabricLoader;

public class UtilsImpl {
    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static int nextTabId() {
        return ItemGroupUtil.expandArrayAndGetId();
    }
}
