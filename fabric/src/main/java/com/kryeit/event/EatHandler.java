package com.kryeit.event;

import com.kryeit.MissionHandler;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.EatMission;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EatHandler implements LivingEntityEvents.FinishUsingItem {

    @Override
    public @Nullable ItemStack modifyUseResult(LivingEntity entity, ItemStack itemStack, ItemStack used) {
        if(entity instanceof Player player) {
            if(MissionHandler.isNotServerPlayer(player)) return null;
            if(!itemStack.isEdible()) return null;
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
            MissionManager.incrementMission(player.getUUID(), EatMission.class, item, 1);
        }
        return null;
    }
}
