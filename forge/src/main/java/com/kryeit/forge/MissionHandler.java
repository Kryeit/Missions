package com.kryeit.forge;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class MissionHandler {
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (isNotServerPlayer(event.getPlayer())) return;
        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock());
        MissionManager.incrementMission(event.getPlayer().getUUID(), BreakMission.class, block, 1);
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;

        ResourceLocation block = BuiltInRegistries.BLOCK.getKey(event.getPlacedBlock().getBlock());

        MissionManager.incrementMission(event.getEntity().getUUID(), PlaceMission.class, block, 1);
    }

    @SubscribeEvent
    public void fishItem(ItemFishedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;

        for (ItemStack itemStack : event.getDrops()) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
            MissionManager.incrementMission(event.getEntity().getUUID(), FishMission.class, item, 1);
        }
    }

    @SubscribeEvent
    public void killEntity(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {

            ResourceLocation entity = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
            MissionManager.incrementMission(player.getUUID(), KillMission.class, entity, 1);
        }
    }

    @SubscribeEvent
    public void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(event.getCrafting().getItem());
        MissionManager.incrementMission(event.getEntity().getUUID(), CraftMission.class, item, 1);
    }

    private boolean isNotServerPlayer(Entity entity) {
        return !(entity instanceof ServerPlayer);
    }
}
