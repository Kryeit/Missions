package com.kryeit.forge;

import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.BreakMission;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MissionHandler {
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        ResourceLocation item = event.getState().getBlock().getRegistryName();
        MissionTypeRegistry.INSTANCE.getType(BreakMission.class).handleBreak(event.getPlayer().getUUID(), item);
    }
}
