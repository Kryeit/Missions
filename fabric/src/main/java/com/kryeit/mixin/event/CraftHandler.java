package com.kryeit.mixin.event;

import com.kryeit.MissionHandler;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.CraftMission;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class CraftHandler {
    @Inject(
            method = "onCraftedBy",
            at = @At("RETURN")
    )
    private void onCraftedBy(Level level, Player player, int i, CallbackInfo ci) {
        if(!MissionHandler.isNotServerPlayer(player)) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(((ItemStack) (Object) this).getItem());
            MissionManager.incrementMission(player.getUUID(), CraftMission.class, item, i);
        }
    }
}
