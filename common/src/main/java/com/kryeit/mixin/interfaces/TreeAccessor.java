package com.kryeit.mixin.interfaces;

import com.simibubi.create.foundation.utility.TreeCutter;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TreeCutter.Tree.class)
public interface TreeAccessor {

    @Accessor("logs")
    List<BlockPos> getLogs();

    @Accessor("leaves")
    List<BlockPos> getLeaves();
}
