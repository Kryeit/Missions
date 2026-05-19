package com.kryeit.utils.forge;

import net.minecraftforge.fml.ModList;

public class UtilsImpl {
    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
}
