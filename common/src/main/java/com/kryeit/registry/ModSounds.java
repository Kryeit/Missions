package com.kryeit.registry;

import com.kryeit.Main;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static com.kryeit.Main.MOD_ID;

public class ModSounds {
    public static final RegistryEntry<SoundEvent> MISSION_COMPLETE = Main.registrate()
            .simple(
                    "mission_complete",
                    Registries.SOUND_EVENT,
                    () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(MOD_ID, "mission_complete"), 1f)
            );

    public static void register() {
    }
}
