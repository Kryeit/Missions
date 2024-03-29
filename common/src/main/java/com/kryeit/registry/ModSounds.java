package com.kryeit.registry;

import com.kryeit.Missions;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final RegistryEntry<SoundEvent> MISSION_COMPLETE = Missions.registrate()
            .simple(
                    "mission_complete",
                    Registries.SOUND_EVENT,
                    () -> SoundEvent.createVariableRangeEvent(Missions.asResource("mission_complete"))
            );

    public static void register() {
    }
}
