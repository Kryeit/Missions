package com.kryeit.forge;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MissionHandler {
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (isNotServerPlayer(event.getPlayer())) return;
        ResourceLocation item = event.getState().getBlock().getRegistryName();
        MissionManager.incrementMission(event.getPlayer().getUUID(), BreakMission.class, item, 1);
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;
        ResourceLocation block = event.getPlacedBlock().getBlock().getRegistryName();

        MissionManager.incrementMission(event.getEntity().getUUID(), PlaceMission.class, block, 1);
    }

    @SubscribeEvent
    public void fishItem(ItemFishedEvent event) {
        if (isNotServerPlayer(event.getPlayer())) return;

        for (ItemStack itemStack : event.getDrops()) {
            ResourceLocation item = itemStack.getItem().getRegistryName();
            MissionManager.incrementMission(event.getPlayer().getUUID(), FishMission.class, item, 1);
        }
    }


    @SubscribeEvent
    public void eatItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceLocation item = event.getItem().getItem().getRegistryName();
            MissionManager.incrementMission(player.getUUID(), EatMission.class, item, 1);
        }
    }

    @SubscribeEvent
    public void killEntity(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ResourceLocation entity = event.getEntity().getType().getRegistryName();
            MissionManager.incrementMission(player.getUUID(), KillMission.class, entity, 1);
        }
    }

    @SubscribeEvent
    public void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;
        ResourceLocation item = event.getCrafting().getItem().getRegistryName();
        MissionManager.incrementMission(event.getPlayer().getUUID(), CraftMission.class, item, 1);
    }

    @SubscribeEvent
    public void useItem(PlayerInteractEvent event) {
        event.getItemStack();
    }

    private boolean isNotServerPlayer(Entity entity) {
        return !(entity instanceof ServerPlayer);
    }
}
