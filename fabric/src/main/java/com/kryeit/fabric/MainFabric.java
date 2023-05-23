package com.kryeit.fabric;

import com.kryeit.Main;
import com.kryeit.fabric.packet.ActiveMissionProviderImpl;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class MainFabric implements ModInitializer {
    public static final ResourceLocation OPEN_GUI_PACKET = new ResourceLocation(Main.MOD_ID, "open_gui");

    @Override
    public void onInitialize() {
        Main.init();
    }
}
