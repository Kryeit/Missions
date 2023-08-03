package com.kryeit.utils.forge;

import com.kryeit.utils.ItemHandlerCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

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
        itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        // If ItemStackHandler does not have a similar method that also takes an amount parameter,
        // you might have to adjust this method to fit your needs.
        itemHandler.setStackInSlot(slot, stack);
    }

    public static ItemHandlerCompat create(BlockEntity blockEntity, int size) {
        return new ItemHandlerCompatImpl(blockEntity, size);
    }
}

