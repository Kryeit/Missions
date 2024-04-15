package com.kryeit.registry;

import com.kryeit.Missions;
import com.kryeit.content.exchanger.ponder.MechanicalExchangerScene;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;

public class ModPonders {

    private static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(Missions.MOD_ID);

    public static void register() {
        HELPER.forComponents(ModBlocks.MECHANICAL_EXCHANGER)
                .addStoryBoard("mechanical_exchanger", MechanicalExchangerScene::showPonder);
    }

}
