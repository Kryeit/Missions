package com.kryeit;

import com.kryeit.registry.ModPonders;
import com.kryeit.registry.ModStats;

public class MissionsClient {

    public static void initializeClient() {
        ModPonders.register();
        ModStats.register();

    }
}
