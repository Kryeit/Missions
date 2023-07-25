package com.kryeit.forge;

import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.BreakMission;
import com.kryeit.missions.mission_types.CraftMission;
import com.kryeit.missions.mission_types.EatMission;
import com.kryeit.missions.mission_types.KillMission;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MissionHandler {
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (isNotServerPlayer(event.getPlayer())) return;
        ResourceLocation item = event.getState().getBlock().getRegistryName();
        MissionTypeRegistry.INSTANCE.getType(BreakMission.class).handleItem(event.getPlayer().getUUID(), item);
    }

    @SubscribeEvent
    public void eatItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceLocation item = event.getItem().getItem().getRegistryName();
            MissionTypeRegistry.INSTANCE.getType(EatMission.class).handleItem(player.getUUID(), item);
        }
    }

    @SubscribeEvent
    public void killEntity(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ResourceLocation entity = event.getEntity().getType().getRegistryName();
            MissionTypeRegistry.INSTANCE.getType(KillMission.class).handleItem(player.getUUID(), entity);
        }
    }

    @SubscribeEvent
    public void craftItem(PlayerEvent.ItemCraftedEvent event) {
        if (isNotServerPlayer(event.getEntity())) return;
        ResourceLocation item = event.getCrafting().getItem().getRegistryName();
        MissionTypeRegistry.INSTANCE.getType(CraftMission.class).handleItem(event.getPlayer().getUUID(), item);
    }

    @SubscribeEvent
    public void useItem(PlayerInteractEvent event) {
        event.getItemStack();
    }

    private boolean isNotServerPlayer(Entity entity) {
        return !(entity instanceof ServerPlayer);
    }
}
