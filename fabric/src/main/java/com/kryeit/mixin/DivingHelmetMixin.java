package com.kryeit.mixin;

import com.kryeit.missions.mission_types.create.diving.DivingMissionType;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {
    @Inject(method = "breatheUnderwater", at = @At("RETURN"))
    private static void onDive(LivingEntity entity, CallbackInfo ci) {
        if (!(entity instanceof ServerPlayer player)) return;

        ItemStack helmet = getWornItem(entity);
        if (helmet.isEmpty())
            return;

        boolean lavaDiving = entity.isInLava();
        if (!helmet.getItem()
                .isFireResistant() && lavaDiving)
            return;
        if (!entity.isEyeInFluid(AllTags.AllFluidTags.DIVING_FLUIDS.tag) && !lavaDiving)
            return;

        List<ItemStack> backtanks = BacktankUtil.getAllWithAir(entity);
        if (backtanks.isEmpty())
            return;

        if (lavaDiving) {
            DivingMissionType.handleTimeChange(player.getUUID(), 1, new ResourceLocation("minecraft", "lava"));
        } else {
            BlockState blockState = entity.level.getBlockState(new BlockPos(entity.getEyePosition()));

            DivingMissionType.handleTimeChange(player.getUUID(), 1,
                    Registry.FLUID.getKey(blockState.getFluidState().getType()));
        }
    }

}
