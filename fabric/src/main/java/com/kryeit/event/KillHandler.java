package com.kryeit.event;

import com.kryeit.MissionHandler;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.KillMission;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class KillHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity {

    @Override
    public void afterKilledOtherEntity(ServerLevel world, Entity playerEntity, LivingEntity killedEntity) {
        Player player = null;
        if (playerEntity instanceof Player) player = (Player) playerEntity;
        if (player == null) return;
        if (MissionHandler.isNotServerPlayer(player)) return;

        ResourceLocation entity = BuiltInRegistries.ENTITY_TYPE.getKey(killedEntity.getType());
        MissionManager.incrementMission(player.getUUID(), KillMission.class, entity, 1);
    }
}
