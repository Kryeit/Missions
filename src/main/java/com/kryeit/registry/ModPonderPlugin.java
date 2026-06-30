package com.kryeit.registry;

import com.kryeit.Missions;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return Missions.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ModPonders.register(helper);
    }
}
