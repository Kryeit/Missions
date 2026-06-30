package com.kryeit.event;

import com.kryeit.Missions;
import com.kryeit.command.MissionsCommand;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.BreakMission;
import com.kryeit.missions.mission_types.vanilla.CraftMission;
import com.kryeit.missions.mission_types.vanilla.FishMission;
import com.kryeit.missions.mission_types.vanilla.KillMission;
import com.kryeit.missions.mission_types.vanilla.PlaceMission;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Server-side gameplay hooks for the vanilla mission types and the /missions command.
 * Create-driven mission tracking lives in the mixins under com.kryeit.mixin.create.
 */
public class MissionEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MissionsCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Missions.handlePlayerLogin(event.getEntity());
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock());
        MissionManager.incrementMission(player.getUUID(), BreakMission.class, block, 1);
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(event.getPlacedBlock().getBlock());
        MissionManager.incrementMission(player.getUUID(), PlaceMission.class, block, 1);
    }

    @SubscribeEvent
    public static void onFish(ItemFishedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        for (ItemStack stack : event.getDrops()) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(stack.getItem());
            MissionManager.incrementMission(player.getUUID(), FishMission.class, item, 1);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ResourceLocation entity = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
            MissionManager.incrementMission(player.getUUID(), KillMission.class, entity, 1);
        }
    }

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(event.getCrafting().getItem());
        MissionManager.incrementMission(player.getUUID(), CraftMission.class, item, 1);
    }
}
