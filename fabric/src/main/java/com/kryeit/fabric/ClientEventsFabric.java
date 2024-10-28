package com.kryeit.fabric;

import com.kryeit.events.ClientEvents;
import io.github.fabricators_of_create.porting_lib.event.client.KeyInputCallback;

public class ClientEventsFabric {
    public static void init() {
        KeyInputCallback.EVENT.register((key, scancode, action, mods) -> {
            ClientEvents.onKeyInput(key, action != 0);
        });
    }
}
