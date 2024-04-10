package com.kryeit.mixin.events;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.DrinkMission;
import com.kryeit.registry.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class DrinkHandler {

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    public void onDrinkAnyBeverage(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack itemStack = player.getUseItem();

        if (ModTags.isDrink(itemStack)) {
            MissionManager.incrementMission(player.getUUID(), DrinkMission.class, BuiltInRegistries.ITEM.getKey(itemStack.getItem()), 1);
        }
    }
}
