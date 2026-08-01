package com.kryeit.mixin;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.vanilla.CraftMission;
import com.kryeit.utils.MixinUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credits the vanilla 1.21 Crafter (the autocrafter) toward {@link CraftMission}, attributing the crafted
 * item to the nearest player, exactly like Create's Mechanical Crafter mixin does. Hooks the moment the
 * assembled result is finalized in {@code CrafterBlock.dispenseFrom}.
 */
@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

    @Inject(method = "dispenseFrom", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;onCraftedBySystem(Lnet/minecraft/world/level/Level;)V"))
    private void missions$onAutoCraft(BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci, @Local ItemStack result) {
        if (result == null || result.isEmpty()) return;
        Player player = MixinUtils.getClosestPlayer(level, pos);
        if (player != null) {
            MissionManager.incrementMission(player.getUUID(), CraftMission.class,
                    BuiltInRegistries.ITEM.getKey(result.getItem()), result.getCount());
        }
    }
}
