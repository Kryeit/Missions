package com.kryeit.mixin.events;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.FeedMission;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public class FeedHandler {

    @Inject(method = "usePlayerItem", at = @At("HEAD"))
    private void onFeed(Player player, InteractionHand interactionHand, ItemStack itemStack, CallbackInfo ci) {
        MissionManager.incrementMission(player.getUUID(), FeedMission.class, BuiltInRegistries.ITEM.getKey(itemStack.getItem()), 1);
    }
}
