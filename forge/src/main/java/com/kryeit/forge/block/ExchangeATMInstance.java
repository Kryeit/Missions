package com.kryeit.forge.block;

import com.jozufozu.flywheel.api.MaterialManager;
import com.kryeit.forge.block.entity.custom.ExchangeATMBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class ExchangeATMInstance extends KineticBlockEntityInstance<ExchangeATMBlockEntity> {

    protected final RotatingData shaft;
    final Direction direction;
    private final Direction opposite;



    public ExchangeATMInstance(MaterialManager materialManager, ExchangeATMBlockEntity blockEntity) {
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
