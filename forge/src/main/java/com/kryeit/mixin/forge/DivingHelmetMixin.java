package com.kryeit.mixin.forge;

import com.kryeit.missions.mission_types.create.diving.DivingMissionType;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {
    @Inject(method = "breatheUnderwater", at = @At("HEAD"))
    private static void onDive(LivingEvent.LivingUpdateEvent event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        LivingEntity entity = event.getEntityLiving();

        ItemStack helmet = getWornItem(entity);
        if (helmet.isEmpty())
            return;

        boolean lavaDiving = entity.isInLava();
        if (!helmet.getItem()
                .isFireResistant() && lavaDiving)
            return;
        if (!entity.isEyeInFluid(FluidTags.WATER) && !lavaDiving)
            return;
        if (entity instanceof Player && ((Player) entity).isCreative())
            return;

        List<ItemStack> backtanks = BacktankUtil.getAllWithAir(entity);
        if (backtanks.isEmpty())
            return;

        if (lavaDiving) {
            DivingMissionType.handleTimeChange(player.getUUID(), 1, new ResourceLocation("minecraft", "lava"));
        } else {
            BlockState blockState = event.getEntity().level.getBlockState(new BlockPos(event.getEntity().getEyePosition()));

            DivingMissionType.handleTimeChange(player.getUUID(), 1,
                    ForgeRegistries.FLUIDS.getKey(blockState.getFluidState().getType()));
        }
    }

}
