package com.kryeit.mixin.event;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.FishMission;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(FishingRodHookedTrigger.class)
public class FishHandler {
    @Inject(method = "trigger", at = @At("HEAD"))
    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack, FishingHook fishingHook, Collection<ItemStack> collection, CallbackInfo ci) {
        for (ItemStack stack : collection) {
            ResourceLocation item = stack.getItem().getRegistryName();
            MissionManager.incrementMission(serverPlayer.getUUID(), FishMission.class, item, stack.getCount());
        }
    }
}
