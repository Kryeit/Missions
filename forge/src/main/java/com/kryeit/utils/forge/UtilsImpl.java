package com.kryeit.utils.forge;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.ModList;

public class UtilsImpl {
    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    public static int nextTabId() {
        return CreativeModeTab.getGroupCountSafe();
    }
}
