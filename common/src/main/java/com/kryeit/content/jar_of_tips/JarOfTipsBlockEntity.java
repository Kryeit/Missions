package com.kryeit.content.jar_of_tips;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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

    public NonNullList<ItemStack> inventory ;

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
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.inventory.get(i);
        if (itemStack.isEmpty())
            return ItemStack.EMPTY;
        this.inventory.set(i, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.inventory.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
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
            itemStack.isEmpty();
        }
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
        this.setChanged();
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
}
