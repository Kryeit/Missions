package com.kryeit.content.exchanger;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public class MechanicalExchangerInstance extends KineticBlockEntityVisual<MechanicalExchangerBlockEntity> {

    protected final RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;

    public MechanicalExchangerInstance(VisualizationContext context, MechanicalExchangerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        opposite = direction.getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();

        shaft.setup(blockEntity, rotationAxis(), blockEntity.getSpeed())
                .setChanged();

    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
    }

    @Override
    public void update(float pt) {
        shaft.setup(blockEntity, rotationAxis(), blockEntity.getSpeed())
                .setChanged();
    }

    @Override
    public void updateLight(float v) {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);
    }

    @Override
    protected void _delete() {
        shaft.delete();
    }
}
