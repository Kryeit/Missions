package com.kryeit.entry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import static com.kryeit.Main.MOD_ID;
import static com.kryeit.Main.REGISTRATE;

public class ModSounds {
    public static final RegistryEntry<SoundEvent> MISSION_COMPLETE = REGISTRATE
            .simple(
                    "mission_complete",
                    Registry.SOUND_EVENT_REGISTRY,
                    () -> new SoundEvent(new ResourceLocation(MOD_ID, "mission_complete"))
            );

    public static void register() {
    }
}
