package com.kryeit.registry;

import com.kryeit.content.exchanger.ponder.MechanicalExchangerScene;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonders {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.forComponents(ModBlocks.MECHANICAL_EXCHANGER)
                .addStoryBoard("mechanical_exchanger", MechanicalExchangerScene::showPonder);
    }
}
