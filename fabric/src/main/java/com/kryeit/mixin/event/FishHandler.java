package com.kryeit.mixin.event;

import com.kryeit.missions.MissionManager;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.UUID;

@Mixin(FishingRodHookedTrigger.class)
public class FishHandler {
    @Inject(method = "trigger", at = @At("HEAD"))
    public void goBucks(ServerPlayer player, ItemStack usedItem, FishingHook fishingHook, Collection<ItemStack> loot, CallbackInfo ci) {
        UUID uuid = player.getUUID();

        loot.forEach(itemStack -> {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
            MissionManager.incrementMission(uuid, "fish", key, itemStack.getCount());
        });
    }
}
