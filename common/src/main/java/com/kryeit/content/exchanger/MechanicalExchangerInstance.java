package com.kryeit.content.exchanger;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class MechanicalExchangerInstance extends KineticBlockEntityInstance<MechanicalExchangerBlockEntity> {

    protected final RotatingData shaft;
    final Direction direction;
    private final Direction opposite;

    public MechanicalExchangerInstance(MaterialManager materialManager, MechanicalExchangerBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, opposite).createInstance();

        setup(shaft);
    }

    @Override
    public void update() {
        updateRotation(shaft);
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);
    }

    @Override
    protected void remove() {
        shaft.delete();
    }
}
