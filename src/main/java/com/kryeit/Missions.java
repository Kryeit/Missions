package com.kryeit;

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
import com.kryeit.missions.mission_types.create.belt.BeltWalkMission;
import com.kryeit.missions.mission_types.create.contraption.DrillMission;
import com.kryeit.missions.mission_types.create.contraption.HarvestMission;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.kryeit.missions.mission_types.create.diving.DivingMission;
import com.kryeit.missions.mission_types.create.train.TrainDriverMission;
import com.kryeit.missions.mission_types.create.train.TrainDriverPassengerMission;
import com.kryeit.missions.mission_types.create.train.TrainPassengerMission;
import com.kryeit.missions.mission_types.create.train.TrainRelocateMission;
import com.kryeit.missions.mission_types.vanilla.*;
import com.kryeit.registry.ModBlockEntities;
import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModCreativeTabs;
import com.kryeit.registry.ModEntityTypes;
import com.kryeit.registry.ModItems;
import com.kryeit.registry.ModMenuTypes;
import com.kryeit.utils.Lang;
import com.kryeit.utils.Utils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod(Missions.MOD_ID)
public class Missions {
    public static final String MOD_ID = "missions";
    public static final String NAME = "Missions";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    private static ConfigReader configReader;

    public static HashMap<UUID, Vec3> cachedTrainPlayerPositions = new HashMap<>();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public Missions(IEventBus modEventBus, ModContainer modContainer) {
        registerMissions();

        // Content registration via Create Registrate (blocks, block entities, items, menus, entity, creative tab).
        ModCreativeTabs.register(modEventBus);
        REGISTRATE.defaultCreativeTab(ModCreativeTabs.getBaseTabKey());
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModMenuTypes.register();
        ModEntityTypes.register();
        REGISTRATE.registerEventListeners(modEventBus);

        modEventBus.addListener(this::commonSetup);

        // Server-side gameplay event handlers + the /missions command live on the game bus.
        NeoForge.EVENT_BUS.register(com.kryeit.event.MissionEvents.class);

        LOGGER.info("{} loaded for NeoForge 1.21.1", NAME);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        readConfig();
        event.enqueueWork(ModBlocks::registerStresses);
    }

    public static ResourceLocation asResource(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }

    public static void handlePlayerLogin(Player player) {
        boolean reassigned = MissionManager.reassignMissionsIfNecessary(player.getUUID());
        if (reassigned) {
            player.sendSystemMessage(Lang.t("missions.reassign",
                    "Your missions have been refreshed! Open them with /missions").withStyle(ChatFormatting.GREEN));
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
        MissionTypeRegistry.INSTANCE.register(new FeedMission());
        MissionTypeRegistry.INSTANCE.register(new PressMission());
        MissionTypeRegistry.INSTANCE.register(new BeltWalkMission());

        // Basin
        MissionTypeRegistry.INSTANCE.register(new CompactMission());
        MissionTypeRegistry.INSTANCE.register(new MixMission());

        // Train
        MissionTypeRegistry.INSTANCE.register(new TrainDriverMission());
        MissionTypeRegistry.INSTANCE.register(new TrainPassengerMission());
        MissionTypeRegistry.INSTANCE.register(new TrainDriverPassengerMission());
        MissionTypeRegistry.INSTANCE.register(new TrainRelocateMission());

        // Dive
        MissionTypeRegistry.INSTANCE.register(new DivingMission());

        // Contraption
        MissionTypeRegistry.INSTANCE.register(new DrillMission());
        MissionTypeRegistry.INSTANCE.register(new SawMission());
        MissionTypeRegistry.INSTANCE.register(new HarvestMission());

        List.of(
                StatisticMission.createStatisticMission(
                        "walk",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Walking mission"),
                        100,
                        Items.LEATHER_BOOTS,
                        Stats.WALK_ONE_CM,
                        Stats.SPRINT_ONE_CM,
                        Stats.CROUCH_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "swim",
                        MissionDifficulty.HARD,
                        Component.nullToEmpty("Swimming mission"),
                        100,
                        Items.TURTLE_HELMET,
                        Stats.SWIM_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "minecart",
                        MissionDifficulty.NORMAL,
                        Component.nullToEmpty("Minecart mission"),
                        100,
                        Items.MINECART,
                        Stats.MINECART_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "ride",
                        MissionDifficulty.HARD,
                        Component.nullToEmpty("Riding mission"),
                        100,
                        Items.SADDLE,
                        Stats.HORSE_ONE_CM,
                        Stats.PIG_ONE_CM,
                        Stats.STRIDER_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "fly",
                        MissionDifficulty.EASY,
                        Component.nullToEmpty("Flying mission"),
                        100,
                        Items.ELYTRA,
                        Stats.AVIATE_ONE_CM
                ),
                StatisticMission.createStatisticMission(
                        "sail",
                        MissionDifficulty.NORMAL,
                        Component.nullToEmpty("Sailing mission"),
                        100,
                        Items.OAK_BOAT,
                        Stats.BOAT_ONE_CM
                )
        ).forEach(MissionTypeRegistry.INSTANCE::register);
    }

    public static void readConfig() {
        try {
            LOGGER.info("Reading config file...");
            configReader = ConfigReader.readFile(Path.of("config/missions"));
            List<MissionType> unusedTypes = new ArrayList<>(MissionTypeRegistry.INSTANCE.getAllTypes());
            unusedTypes.removeAll(configReader.getMissions().keySet());

            if (!unusedTypes.isEmpty()) {
                LOGGER.warn(
                        "The following mission types are available but ignored due to having a weight of 0 or being absent in the config file: {}",
                        Utils.map(unusedTypes, MissionType::id)
                );
            }
            if (configReader.getMissions().size() < 10) {
                if (Utils.filter(configReader.getMissions().keySet(), MissionType::assignOnlyOnce).size() < configReader.getMissions().size()) {
                    LOGGER.warn("Mission types will be assigned more than once per player since less than 10 missions are active");
                } else {
                    throw new RuntimeException("10 missions can not be selected per player since less than 10 mission types that can be assigned at most once are enabled and no multiple assignable type is enabled.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
