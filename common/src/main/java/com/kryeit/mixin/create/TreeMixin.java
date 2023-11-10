package com.kryeit.mixin.create;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.create.contraption.SawMission;
import com.simibubi.create.foundation.utility.TreeCutter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static com.kryeit.utils.MixinUtils.getClosestPlayer;

@Mixin(value = TreeCutter.Tree.class, remap = false)
public class TreeMixin {

    @Shadow @Final private List<BlockPos> leaves;

    @Shadow @Final private List<BlockPos> logs;

    @Inject(method = "destroyBlocks", at = @At("HEAD"))
    public void destroyBlocks(Level world, ItemStack toDamage, Player playerEntity, BiConsumer<BlockPos, ItemStack> drop, CallbackInfo ci) {
        List<BlockPos> all = new ArrayList<>(logs);
        if (logs.isEmpty()) return;

        // TODO: For some reason there is more BlockPos for leaves than it should (like 3x)
        all.addAll(leaves);
        
        final Player closestPlayer = getClosestPlayer(world, logs.get(0));

        if (closestPlayer == null) return;

        all.forEach(pos -> {
            MissionManager.incrementMission(
                    closestPlayer.getUUID(),
                    SawMission.class,
                    Registry.ITEM.getKey(world.getBlockState(pos).getBlock().asItem()),
                    1);
        });
    }
}
