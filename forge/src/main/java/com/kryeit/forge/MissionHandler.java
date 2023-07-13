package com.kryeit.forge;

public class MissionHandler {
    //TODO: THIS DOESNT WORK, SAME ISSUE AS IN THE PLAYER LOGIN, I THINK ONLY THE useItem() method IS WRONG
    //@SubscribeEvent
    //public void breakBlock(BlockEvent.BreakEvent event) {
    //    ResourceLocation item = event.getState().getBlock().getRegistryName();
    //    MissionTypeRegistry.INSTANCE.getType(BreakMission.class).handleItem(event.getPlayer().getUUID(), item);
    //}
//
    //@SubscribeEvent
    //public void eatItem(LivingEntityUseItemEvent.Finish event) {
    //    if (event.getEntity() instanceof Player player) {
    //        ResourceLocation item = event.getItem().getItem().getRegistryName();
    //        MissionTypeRegistry.INSTANCE.getType(EatMission.class).handleItem(player.getUUID(), item);
    //    }
    //}
//
    //@SubscribeEvent
    //public void killEntity(LivingDeathEvent event) {
    //    if (event.getSource().getEntity() instanceof Player player) {
    //        ResourceLocation entity = event.getEntity().getType().getRegistryName();
    //        MissionTypeRegistry.INSTANCE.getType(KillMission.class).handleItem(player.getUUID(), entity);
    //    }
    //}
//
    //@SubscribeEvent
    //public void craftItem(PlayerEvent.ItemCraftedEvent event) {
    //    ResourceLocation item = event.getCrafting().getItem().getRegistryName();
    //    MissionTypeRegistry.INSTANCE.getType(CraftMission.class).handleItem(event.getPlayer().getUUID(), item);
    //}
//
    //@SubscribeEvent
    //public void useItem(PlayerInteractEvent event) {
    //    event.getItemStack();
    //}
}
