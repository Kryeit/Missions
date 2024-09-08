package com.kryeit;

import com.kryeit.registry.ModKeys;
import com.kryeit.registry.ModPonders;

public class MissionsClient {

    public static void initializeClient() {
        ModPonders.register();
        ModKeys.register();
    }
}
