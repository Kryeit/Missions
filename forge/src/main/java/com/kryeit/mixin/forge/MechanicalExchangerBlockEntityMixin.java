package com.kryeit.mixin.forge;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.forge.MechanicalExchangerContainerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(MechanicalExchangerBlockEntity.class)
public abstract class MechanicalExchangerBlockEntityMixin extends BlockEntity {

	private IItemHandler inventory;
	private LazyOptional<IItemHandler> itemOptional;

	MechanicalExchangerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Unique
	private IItemHandler createItemHandler() {
		return this.inventory == null ? this.inventory = new MechanicalExchangerContainerInterface((MechanicalExchangerBlockEntity) (Object) this) : this.inventory;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (ForgeCapabilities.ITEM_HANDLER == cap) {
			if (this.itemOptional == null)
				this.itemOptional = LazyOptional.of(this::createItemHandler);
			return this.itemOptional.cast();
		}
		return super.getCapability(cap, side);
	}

}
