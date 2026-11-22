package com.kryeit.content.exchanger;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import static com.kryeit.content.exchanger.MechanicalExchangerBlockEntity.INPUT_SLOT;
import static com.kryeit.content.exchanger.MechanicalExchangerBlockEntity.OUTPUT_SLOT;

public record MechanicalExchangerItemHandler(MechanicalExchangerBlockEntity be) implements IItemHandler {

    @Override
    public int getSlots() {
        return be.getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return be.getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot != INPUT_SLOT || stack.isEmpty()) return stack;

        ItemStack current = be.getItem(slot);
        if (!current.isEmpty() && !ItemStack.isSameItemSameComponents(current, stack)) return stack;

        int space = Math.min(getSlotLimit(slot), stack.getMaxStackSize()) - current.getCount();
        int accepted = Math.min(space, stack.getCount());
        if (accepted <= 0) return stack;

        if (!simulate) be.setItem(slot, stack.copyWithCount(current.getCount() + accepted));
        return accepted == stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - accepted);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != OUTPUT_SLOT || amount <= 0) return ItemStack.EMPTY;

        ItemStack current = be.getItem(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;

        int extracted = Math.min(amount, current.getCount());
        return simulate ? current.copyWithCount(extracted) : be.removeItem(slot, extracted);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot == INPUT_SLOT;
    }
}
