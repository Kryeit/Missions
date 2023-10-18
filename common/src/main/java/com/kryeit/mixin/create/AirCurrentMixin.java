package com.kryeit.mixin.create;

//@Mixin(AirCurrent.class)
//public class AirCurrentMixin {
//    @Inject(
//            method = "lambda$tickAffectedHandlers$2",
//            at = @At(
//                    value = "RETURN"
//            ),
//            locals = LocalCapture.CAPTURE_FAILSOFT
//    )
//    private void tickAffectedHandlers(Level world, FanProcessing.Type type, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
//        if (world.isClientSide) return;
//        if (cir.getReturnValue().doesNothing()) return;
//
//        List<TransportedItemStack> transportedItemStacks = cir.getReturnValue().getOutputs();
//        List<ItemStack> stacks = new ArrayList<>();
//        transportedItemStacks.forEach(transportedItemStack -> stacks.add(transportedItemStack.stack));
//
//        Player closestPlayer = MixinUtils.getClosestPlayer(world, handler.getPos());
//
//        if (closestPlayer != null) {
//            for (ItemStack stack : stacks) {
//                if (type == FanProcessing.Type.NONE) return;
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
//}
