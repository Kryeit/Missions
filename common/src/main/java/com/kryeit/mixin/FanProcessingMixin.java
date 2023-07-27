package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.Utils;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.create.fan.BlastMission;
import com.kryeit.missions.mission_types.create.fan.HauntMission;
import com.kryeit.missions.mission_types.create.fan.SmokeMission;
import com.kryeit.missions.mission_types.create.fan.SplashMission;
import com.simibubi.create.content.kinetics.fan.FanProcessing;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(FanProcessing.class)
public class FanProcessingMixin {

    @Inject(
            method = "applyProcessing(Lnet/minecraft/world/entity/item/ItemEntity;Lcom/simibubi/create/content/kinetics/fan/FanProcessing$Type;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;isEmpty()Z",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onApplyFanRecipe(ItemEntity entity, FanProcessing.Type type, CallbackInfoReturnable<Boolean> cir, List<ItemStack> stacks) {
        Player closestPlayer = Utils.getClosestPlayer(entity.level, entity.blockPosition());

        if(closestPlayer != null) {
            for(ItemStack stack : stacks) {
                switch (type) {
                    case BLASTING -> MissionTypeRegistry.INSTANCE.getType(BlastMission.class).handleItem(
                            closestPlayer.getUUID(),
                            PlatformSpecific.getResourceLocation(stack.getItem()),
                            stack.getCount());
                    case SMOKING -> MissionTypeRegistry.INSTANCE.getType(SmokeMission.class).handleItem(
                            closestPlayer.getUUID(),
                            PlatformSpecific.getResourceLocation(stack.getItem()),
                            stack.getCount());
                    case SPLASHING -> MissionTypeRegistry.INSTANCE.getType(SplashMission.class).handleItem(
                            closestPlayer.getUUID(),
                            PlatformSpecific.getResourceLocation(stack.getItem()),
                            stack.getCount());
                    case HAUNTING -> MissionTypeRegistry.INSTANCE.getType(HauntMission.class).handleItem(
                            closestPlayer.getUUID(),
                            PlatformSpecific.getResourceLocation(stack.getItem()),
                            stack.getCount());
                }
            }
        }
    }
}

