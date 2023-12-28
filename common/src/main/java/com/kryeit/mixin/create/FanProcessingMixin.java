package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.utils.MixinUtils;
import com.simibubi.create.content.kinetics.fan.FanProcessing;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = FanProcessing.class, remap = false)
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
        Player closestPlayer = MixinUtils.getClosestPlayer(entity.level, entity.blockPosition());

        if (closestPlayer == null) return;

        for (ItemStack stack : stacks) {
            if (type == null || type == FanProcessing.Type.NONE) return;

            String id = switch (type) {
                case SPLASHING -> "splash";
                case SMOKING -> "smoke";
                case HAUNTING -> "haunt";
                case BLASTING -> "blast";
                default -> throw new IllegalStateException("Unexpected fan processing type value: " + type);
            };
            MissionManager.incrementMission(closestPlayer.getUUID(), id, Registry.ITEM.getKey(stack.getItem()), stack.getCount());
        }
    }

    // TODO: Cannot get the blockPos in this function.
//    @Inject(
//            method = "applyProcessing(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/fan/FanProcessing$Type;)Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;",
//            at = @At(
//                    value = "TAIL"
//            )
//    )
//    private static void onApplyProcessing(TransportedItemStack transported, Level level, FanProcessing.Type type, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
//        List<TransportedItemStack> transportedStacks = cir.getReturnValue().getOutputs();
//        List<ItemStack> stacks = new ArrayList<>();
//        for (TransportedItemStack s : transportedStacks) {
//            stacks.add(s.stack);
//        }
//
//        Player closestPlayer = MixinUtils.getClosestPlayer(level, );
//
//        if (closestPlayer != null) {
//            for (ItemStack stack : stacks) {
//                if (type == null || type == FanProcessing.Type.NONE) return;
//
//                String id = switch (type) {
//                    case SPLASHING -> "splash";
//                    case SMOKING -> "smoke";
//                    case HAUNTING -> "haunt";
//                    case BLASTING -> "blast";
//                    default -> throw new IllegalStateException("Unexpected fan processing type value: " + type);
//                };
//                MissionManager.incrementMission(closestPlayer.getUUID(), id, Registry.ITEM.getKey(stack.getItem()), stack.getCount());
//            }
//        }
//    }
}

