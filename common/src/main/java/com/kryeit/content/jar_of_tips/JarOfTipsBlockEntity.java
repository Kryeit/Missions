package com.kryeit.content.jar_of_tips;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarOfTipsBlockEntity extends SmartBlockEntity implements WorldlyContainer, Nameable {

    public NonNullList<ItemStack> inventory;

    @Nullable
    private Component name;

    public JarOfTipsBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    /* Container */

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.inventory.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventory, i, j);
        if (!itemStack.isEmpty())
            this.setChanged();
        updateFillLevel();
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.inventory.get(i);
        if (itemStack.isEmpty())
            return ItemStack.EMPTY;
        this.inventory.set(i, ItemStack.EMPTY);
        updateFillLevel();
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.inventory.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
        updateFillLevel();
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
        updateFillLevel();
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void tick() {
    }

    public void addItemToInventory(ItemStack itemStack, int slot) {
        ItemStack inventoryStack = this.getItem(slot);
        if (inventoryStack.isEmpty() || (ItemStack.isSameItemSameTags(inventoryStack, itemStack) && inventoryStack.getCount() < inventoryStack.getMaxStackSize())) {
            int fit = Math.min(itemStack.getCount(), inventoryStack.getMaxStackSize() - inventoryStack.getCount());
            if (inventoryStack.isEmpty()) {
                this.setItem(slot, itemStack.split(fit));
            } else {
                inventoryStack.grow(fit);
                itemStack.shrink(fit);
            }
            this.setChanged();
            updateFillLevel();
            itemStack.isEmpty();
        }
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
        this.setChanged();
        updateFillLevel();
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    /* Nameable */

    protected Component getDefaultName() {
        return Component.translatable("block.missions.jar_of_tips");
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public void setCustomName(Component component) {
        this.name = component;
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }


    public void updateFillLevel() {
        if (level == null || level.isClientSide) {
            return;
        }

        long filledSlots = inventory.stream().filter(itemStack -> !itemStack.isEmpty()).count();

        int fillLevel;
        if (filledSlots == 0) {
            fillLevel = 0;
        } else if (filledSlots == 9) {
            fillLevel = 3;
        } else if (filledSlots >= 5) {
            fillLevel = 2;
        } else {
            fillLevel = 1;
        }

        BlockState currentState = getBlockState();
        if (currentState.getBlock() instanceof JarOfTipsBlock) {
            if (currentState.getValue(JarOfTipsBlock.FILL_LEVEL) != fillLevel) {
                level.setBlock(worldPosition, currentState.setValue(JarOfTipsBlock.FILL_LEVEL, fillLevel), 3);
            }
        }
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.saveAllItems(tag, inventory);
        super.write(tag, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.loadAllItems(tag, inventory);
        super.read(tag, clientPacket);
    }
}