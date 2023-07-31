package com.kryeit.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemHandlerCompat {

    protected final BlockEntity blockEntity;

    protected ItemHandlerCompat(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @ExpectPlatform
    public static ItemHandlerCompat create(BlockEntity blockEntity, int size) {
        throw new AssertionError();
    }

    public CompoundTag serializeNBT() {
        throw new AssertionError();
    }

    public void deserializeNBT(CompoundTag nbt) {
        throw new AssertionError();
    }

    public int getSlots() {
        throw new AssertionError();
    }

    public ItemStack getStackInSlot(int slot) {
        throw new AssertionError();
    }

    public void extractItem(int slot, int amount, boolean simulate) {
        throw new AssertionError();
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        throw new AssertionError();
    }
}

