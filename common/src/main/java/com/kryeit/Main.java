package com.kryeit;


import com.kryeit.entry.ModBlockEntities;
import com.kryeit.entry.ModBlocks;
import com.kryeit.entry.ModItems;
import com.kryeit.entry.ModMenuTypes;
import com.kryeit.missions.*;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.mission_types.*;
import com.kryeit.missions.mission_types.create.CrushMission;
import com.kryeit.missions.mission_types.create.CutMission;
import com.kryeit.missions.mission_types.create.MillMission;
import com.kryeit.missions.mission_types.create.PressMission;
import com.kryeit.missions.mission_types.create.basin.CompactMission;
import com.kryeit.missions.mission_types.create.basin.MixMission;
import com.kryeit.missions.mission_types.create.fan.BlastMission;
import com.kryeit.missions.mission_types.create.fan.HauntMission;
import com.kryeit.missions.mission_types.create.fan.SmokeMission;
import com.kryeit.missions.mission_types.create.fan.SplashMission;
import com.kryeit.utils.Utils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String MOD_ID = "missions";
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static ConfigReader configReader;

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        registerMissions();

        // Registering
        ModBlocks.register();
        ModItems.register();
        ModMenuTypes.register();
        ModBlockEntities.register();

        try {
            LOGGER.info("Reading config file...");
            configReader = ConfigReader.readFile(Path.of("missions"));
            List<MissionType> unusedTypes = new ArrayList<>(MissionTypeRegistry.INSTANCE.getAllTypes());
            unusedTypes.removeAll(configReader.getMissions().keySet());

            if (!unusedTypes.isEmpty()) {
                LOGGER.warn(
                        "The following mission types are available but ignored due to their absence in the config file: {}",
                        Utils.map(unusedTypes, MissionType::id)
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(DataStorage.INSTANCE::save));
    }

    public static void handlePlayerLogin(Player player) {
        boolean reassigned = MissionManager.reassignMissionsIfNecessary(player.getUUID());
        if (reassigned) {
            // TODO send a message, I don't know?
        }
    }

    public static ConfigReader getConfig() {
        return configReader;
    }

    private static void registerMissions() {
        MissionTypeRegistry.INSTANCE.register(new BreakMission());
        MissionTypeRegistry.INSTANCE.register(new CraftMission());
        MissionTypeRegistry.INSTANCE.register(new CrushMission());
        MissionTypeRegistry.INSTANCE.register(new CutMission());
        MissionTypeRegistry.INSTANCE.register(new EatMission());
        MissionTypeRegistry.INSTANCE.register(new FishMission());
        MissionTypeRegistry.INSTANCE.register(new KillMission());
        MissionTypeRegistry.INSTANCE.register(new MillMission());
        MissionTypeRegistry.INSTANCE.register(new PlaceMission());
        MissionTypeRegistry.INSTANCE.register(new PressMission());
        MissionTypeRegistry.INSTANCE.register(new VoteMission());

        // Basin
        MissionTypeRegistry.INSTANCE.register(new CompactMission());
        MissionTypeRegistry.INSTANCE.register(new MixMission());

        // Fan
        MissionTypeRegistry.INSTANCE.register(new BlastMission());
        MissionTypeRegistry.INSTANCE.register(new HauntMission());
        MissionTypeRegistry.INSTANCE.register(new SmokeMission());
        MissionTypeRegistry.INSTANCE.register(new SplashMission());

        List.of(
                StatisticMission.createStatisticMission(
                        "walk",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Walking mission"),
                        100_000,
                        Items.LEATHER_BOOTS,
                        Stats.WALK_ONE_CM,
                        Stats.SPRINT_ONE_CM,
                        Stats.CROUCH_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "swim",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Swimming mission"),
                        100_000,
                        Items.TURTLE_HELMET,
                        Stats.SWIM_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "minecart",
                        MissionDifficulty.NORMAL,
                        Component.nullToEmpty("Minecart mission"),
                        100_000,
                        Items.MINECART,
                        Stats.MINECART_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "ride",
                        MissionDifficulty.NORMAL,
                        Component.nullToEmpty("Riding mission"),
                        100_000,
                        Items.SADDLE,
                        Stats.HORSE_ONE_CM,
                        Stats.PIG_ONE_CM,
                        Stats.STRIDER_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "fly",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Flying mission"),
                        100_000,
                        Items.ELYTRA,
                        Stats.FLY_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "sail",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Sailing mission"),
                        100_000,
                        Items.OAK_BOAT,
                        Stats.BOAT_ONE_CM
                )
        ).forEach(MissionTypeRegistry.INSTANCE::register);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
