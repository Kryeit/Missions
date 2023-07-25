package com.kryeit.mixin;

import com.kryeit.PlatformSpecific;
import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.mission_types.PressMission;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(value = MechanicalPressBlockEntity.class, remap = false)
public class MechanicalPressBlockEntityMixin {

    @Inject(method = "onItemPressed", at = @At("RETURN"))
    public void onItemPressed(ItemStack result, CallbackInfo ci) {

        BlockEntityAccessor accessor = (BlockEntityAccessor) this;

        Level level = accessor.getLevel();
        BlockPos worldPosition = accessor.getWorldPosition();
        Player closestPlayer = null;

        if(level != null && worldPosition != null)
            closestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 64.0, true);

        if (closestPlayer != null) {
            MissionTypeRegistry.INSTANCE.getType(PressMission.class).handleItem(closestPlayer.getUUID(), PlatformSpecific.getResourceLocation(result.getItem()), result.getCount());
        }
    }
}

