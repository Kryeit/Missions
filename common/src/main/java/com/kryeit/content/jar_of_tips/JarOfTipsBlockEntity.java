package com.kryeit.content.jar_of_tips;

import com.kryeit.coins.Coins;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarOfTipsBlockEntity extends SmartBlockEntity implements WorldlyContainer, Nameable {

    public NonNullList<ItemStack> inventory ;
    private int cooldown = 0;

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
        if (this.level == null || this.level.isClientSide) {
            return;
        }

        if (this.cooldown > 0) {
            this.cooldown--;
        }

        if (this.cooldown == 0) {
            AABB areaToCheck = new AABB(worldPosition.offset(2, 2, 2), worldPosition.offset(-1, 0, -1));
            List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, areaToCheck,
                    item -> !item.isRemoved() && item.isAlive());

            boolean pickedUpAnItem = false;
            for (ItemEntity itemEntity : items) {
                ItemStack itemStack = itemEntity.getItem();

                if (!Coins.isCoin(itemStack)) {
                    continue;
                }

                for (int i = 0; i < getContainerSize(); i++) {
                    if (addItemToInventory(itemStack, i)) {
                        level.playSound(null, worldPosition, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                        itemEntity.setItem(ItemStack.EMPTY);
                        itemEntity.discard();
                        pickedUpAnItem = true;
                        break;
                    }
                }
                if (pickedUpAnItem) {
                    this.cooldown = 40;
                    break;
                }
            }
        }
    }

    private boolean addItemToInventory(ItemStack itemStack, int slot) {
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
            return itemStack.isEmpty();
        }
        return false;
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
