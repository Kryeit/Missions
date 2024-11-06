package com.kryeit.registry;

import com.kryeit.Missions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class ModStats {
    public static ResourceLocation EASY_MISSIONS_COMPLETED = Missions.asResource("easy_missions_completed");
    public static ResourceLocation NORMAL_MISSIONS_COMPLETED = Missions.asResource("normal_missions_completed");
    public static ResourceLocation HARD_MISSIONS_COMPLETED = Missions.asResource("hard_missions_completed");
    public static ResourceLocation MISSIONS_REROLLED = Missions.asResource("missions_rerolled");

    public static void register() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "easy_missions_completed", EASY_MISSIONS_COMPLETED);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "normal_missions_completed", NORMAL_MISSIONS_COMPLETED);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "hard_missions_completed", HARD_MISSIONS_COMPLETED);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "missions_rerolled", MISSIONS_REROLLED);

        Stats.CUSTOM.get(EASY_MISSIONS_COMPLETED, StatFormatter.DEFAULT);
        Stats.CUSTOM.get(NORMAL_MISSIONS_COMPLETED, StatFormatter.DEFAULT);
        Stats.CUSTOM.get(HARD_MISSIONS_COMPLETED, StatFormatter.DEFAULT);
        Stats.CUSTOM.get(MISSIONS_REROLLED, StatFormatter.DEFAULT);
    }

}
