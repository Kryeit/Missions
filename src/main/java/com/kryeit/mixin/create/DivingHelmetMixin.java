package com.kryeit.mixin.create;

import com.kryeit.missions.mission_types.create.diving.DivingMission;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {
    @Inject(method = "breatheUnderwater", at = @At("HEAD"))
    private static void onDive(LivingBreatheEvent event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack helmet = getWornItem(event.getEntity());
        if (helmet.isEmpty())
            return;

        boolean lavaDiving = event.getEntity().isInLava();
        // 1.21: fire resistance is a data component rather than Item#isFireResistant()
        if (!helmet.has(DataComponents.FIRE_RESISTANT) && lavaDiving)
            return;
        if (!event.getEntity().canDrownInFluidType(event.getEntity().getEyeInFluidType()) && !lavaDiving)
            return;

        List<ItemStack> backtanks = BacktankUtil.getAllWithAir(event.getEntity());
        if (backtanks.isEmpty())
            return;

        if (lavaDiving) {
            DivingMission.handleTimeChange(player.getUUID(), 1, ResourceLocation.fromNamespaceAndPath("minecraft", "lava"));
        } else {
            BlockState blockState = event.getEntity().level().getBlockState(BlockPos.containing(event.getEntity().getEyePosition()));

            DivingMission.handleTimeChange(player.getUUID(), 1,
                    NeoForgeRegistries.FLUID_TYPES.getKey(blockState.getFluidState().getFluidType()));
        }
    }

}
