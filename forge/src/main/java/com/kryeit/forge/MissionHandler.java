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
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MissionHandler {
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (isNotServerPlayer(event.getPlayer())) return;
        ResourceLocation block = ForgeRegistries.BLOCKS.getKey(event.getState().getBlock());
        MissionManager.incrementMission(event.getPlayer().getUUID(), BreakMission.class, block, 1);
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;

        ResourceLocation block = ForgeRegistries.BLOCKS.getKey(event.getPlacedBlock().getBlock());

        MissionManager.incrementMission(event.getEntity().getUUID(), PlaceMission.class, block, 1);
    }

    @SubscribeEvent
    public void fishItem(ItemFishedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;

        for (ItemStack itemStack : event.getDrops()) {

            ResourceLocation item = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
            MissionManager.incrementMission(event.getEntity().getUUID(), FishMission.class, item, 1);
        }
    }


    @SubscribeEvent
    public void eatItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceLocation item = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());
            MissionManager.incrementMission(player.getUUID(), EatMission.class, item, 1);
        }
    }

    @SubscribeEvent
    public void killEntity(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {

            ResourceLocation entity = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
            MissionManager.incrementMission(player.getUUID(), KillMission.class, entity, 1);
        }
    }

    @SubscribeEvent
    public void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;
        ResourceLocation item = ForgeRegistries.ITEMS.getKey(event.getCrafting().getItem());
        MissionManager.incrementMission(event.getEntity().getUUID(), CraftMission.class, item, 1);
    }

    private boolean isNotServerPlayer(Entity entity) {
        return !(entity instanceof ServerPlayer);
    }
}
