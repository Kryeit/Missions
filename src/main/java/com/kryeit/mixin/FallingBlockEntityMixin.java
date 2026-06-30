package com.kryeit.mixin;

import com.kryeit.content.jar_of_tips.FallingBlockEntityHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity implements FallingBlockEntityHelper {

    @Shadow
    private BlockState blockState;

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    public BlockState missions$getBlockState() {
        return blockState;
    }

    @Unique
    public void missions$setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }
}
