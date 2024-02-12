package com.kryeit;

import com.kryeit.registry.*;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.config.ConfigReader;
import com.kryeit.missions.mission_types.StatisticMission;
import com.kryeit.missions.mission_types.create.CrushMission;
import com.kryeit.missions.mission_types.create.CutMission;
import com.kryeit.missions.mission_types.create.MillMission;
import com.kryeit.missions.mission_types.create.PressMission;
import com.kryeit.missions.mission_types.create.basin.CompactMission;
import com.kryeit.missions.mission_types.create.basin.MixMission;
import com.kryeit.missions.mission_types.create.contraption.DrillMission;
import com.kryeit.missions.mission_types.create.contraption.HarvestMission;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.kryeit.missions.mission_types.create.diving.DivingMissionType;
import com.kryeit.missions.mission_types.create.train.TrainDriverMissionType;
import com.kryeit.missions.mission_types.create.train.TrainDriverPassengerMissionType;
import com.kryeit.missions.mission_types.create.train.TrainPassengerMissionType;
import com.kryeit.missions.mission_types.vanilla.*;
import com.kryeit.utils.Utils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kryeit.registry.ModCreativeTabs.useBaseTab;

public class Main {
    public static final String MOD_ID = "missions";
    public static final String NAME = "Missions";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    private static ConfigReader configReader;

    public static HashMap<ServerPlayer, Vec3> cachedTrainPlayerPositions = new HashMap<>();

    public static CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        registerMissions();

        // Registering
        ModBlocks.register();
        ModMenuTypes.register();
        ModBlockEntities.register();
        ModCreativeTabs.register();
        ModSounds.register();

        useBaseTab();
        finalizeRegistrate();
    }

    public static void handlePlayerLogin(Player player) {
        boolean reassigned = MissionManager.reassignMissionsIfNecessary(player.getUUID());
        if (reassigned) {
            Component message = Component.translatable("missions.reassign");
            player.sendSystemMessage(message);
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

        // Basin
        MissionTypeRegistry.INSTANCE.register(new CompactMission());
        MissionTypeRegistry.INSTANCE.register(new MixMission());

        // Train
        MissionTypeRegistry.INSTANCE.register(new TrainDriverMissionType());
        MissionTypeRegistry.INSTANCE.register(new TrainPassengerMissionType());
        MissionTypeRegistry.INSTANCE.register(new TrainDriverPassengerMissionType());

        // Dive
        MissionTypeRegistry.INSTANCE.register(new DivingMissionType());

        // Contraption
        MissionTypeRegistry.INSTANCE.register(new DrillMission());
        MissionTypeRegistry.INSTANCE.register(new SawMission());
        MissionTypeRegistry.INSTANCE.register(new HarvestMission());

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
                        MissionDifficulty.HARD,
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
                        MissionDifficulty.HARD,
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
                        Stats.AVIATE_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "sail",
                        MissionDifficulty.NORMAL,
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

    @ExpectPlatform
    public static void finalizeRegistrate() {
        throw new AssertionError();
    }

    public static void readConfig() {
        try {
            LOGGER.info("Reading config file...");
            configReader = ConfigReader.readFile(Path.of("config/missions"));
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
    }

}
