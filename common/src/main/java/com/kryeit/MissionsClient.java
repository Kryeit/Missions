package com.kryeit;

import com.kryeit.registry.ModKeys;
import com.kryeit.registry.ModPonderPlugin;
import com.kryeit.registry.ModPonders;
import net.createmod.ponder.foundation.PonderIndex;

public class MissionsClient {

    public static void initializeClient() {
        ModKeys.register();
        //PonderIndex.addPlugin(new ModPonderPlugin());
    }
}
