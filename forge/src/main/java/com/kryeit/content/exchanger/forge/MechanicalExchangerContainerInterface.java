package com.kryeit.content.exchanger.forge;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static com.kryeit.content.exchanger.MechanicalExchangerBlockEntity.INPUT_SLOT;
import static com.kryeit.content.exchanger.MechanicalExchangerBlockEntity.OUTPUT_SLOT;

public record MechanicalExchangerContainerInterface(MechanicalExchangerBlockEntity be) implements IItemHandler {

	@Override public int getSlots() { return 2; }

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return be.inventory.get(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot != INPUT_SLOT) return stack;

		ItemStack currentStack = this.getStackInSlot(slot);
		if (!currentStack.isEmpty() && !ItemStack.isSameItemSameTags(currentStack, stack))
			return stack;

		int canAdd = Math.min(stack.getCount(), this.getSlotLimit(slot) - currentStack.getCount());
		if (canAdd < 1) return stack;

		if (!simulate) {
			if (currentStack.isEmpty()) {
				ItemStack copy = stack.copy();
				copy.setCount(canAdd);
				this.be.setItem(slot, copy);
			} else {
				currentStack.grow(canAdd);
			}
			stack.shrink(canAdd);
			this.be.setChanged();
		}

		ItemStack ret = stack.copy();
		ret.shrink(canAdd);
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != OUTPUT_SLOT) return ItemStack.EMPTY;

		ItemStack currentStack = this.getStackInSlot(slot);
		if (currentStack.isEmpty()) return ItemStack.EMPTY;

		int maxExtract = Math.min(amount, currentStack.getCount());
		if (maxExtract <= 0) return ItemStack.EMPTY;

		if (!simulate) {
			currentStack.shrink(maxExtract);
		}
		return new ItemStack(currentStack.getItem(), maxExtract);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return true; }

}
