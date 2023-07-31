package com.kryeit.utils.fabric;

import com.kryeit.utils.ItemHandlerCompat;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemHandlerCompatImpl extends ItemHandlerCompat {
    private final ItemStackHandler itemHandler;

    public ItemHandlerCompatImpl(BlockEntity blockEntity, int size) {
        super(blockEntity);
        this.itemHandler = new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                blockEntity.setChanged();
            }
        };
    }

    @Override
    public CompoundTag serializeNBT() {
        return itemHandler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt);
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public void extractItem(int slot, int amount, boolean simulate) {
        ItemStack existingStack = itemHandler.getStackInSlot(slot);
        int existingAmount = existingStack.getCount();

        ItemStack output = existingStack.copy();
        output.setCount(existingAmount - amount);

        itemHandler.setStackInSlot(slot, output);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }
}
