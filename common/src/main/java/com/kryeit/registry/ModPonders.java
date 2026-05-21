package com.kryeit.registry;

import com.kryeit.content.exchanger.ponder.MechanicalExchangerScene;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonders {
    @ExpectPlatform
    public static ResourceLocation getId(RegistryEntry<?, ?> entry) {
        throw new AssertionError();
    }

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(ModPonders::getId);
        HELPER.forComponents(ModBlocks.MECHANICAL_EXCHANGER)
                .addStoryBoard("mechanical_exchanger", MechanicalExchangerScene::showPonder);
    }
}
