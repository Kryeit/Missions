package com.kryeit.content.atm;

import com.kryeit.coins.Coins;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ExchangeATMBlockEntity extends KineticBlockEntity implements MenuProvider, WorldlyContainer {

    public NonNullList<ItemStack> inventory;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 64;

    Mode mode;

    enum Mode {
        TO_SMALLER, TO_BIGGER, OFF
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return i == 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return i == 1;
    }

    @Override
    public int getContainerSize() {
        return 2;
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
    public @NotNull ItemStack getItem(int i) {
        return this.inventory.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventory, i, j);
        if (!itemStack.isEmpty())
            this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.inventory.get(i);
        if (itemStack.isEmpty())
            return ItemStack.EMPTY;
        this.inventory.set(i, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        this.inventory.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(5);

        this.inventory.forEach(stack ->
                container.setItem(this.inventory.indexOf(stack), stack)
        );

        return container;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {

    }

    public ExchangeATMBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(blockEntityType, pWorldPosition, pBlockState);

        this.mode = Mode.OFF;

        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ExchangeATMBlockEntity.this.progress;
                    case 1 -> ExchangeATMBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ExchangeATMBlockEntity.this.progress = value;
                    case 1 -> ExchangeATMBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Nonnull
    @Override
    public Component getDisplayName() {

        return new TextComponent("Exchange ATM - Mode: " + mode);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return ExchangeATMMenu.create(pContainerId, pInventory, this, data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.saveAllItems(tag, inventory);

        if (!clientPacket) {
            tag.putInt("exchange_atm.progress", progress);
        }
        super.write(tag, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.loadAllItems(tag, inventory);
        if (!clientPacket) {
            progress = tag.getInt("exchange_atm.progress");
        }
        super.read(tag, clientPacket);
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState, ExchangeATMBlockEntity pBlockEntity) {
        updateMode();
        if(hasRecipe(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if(pBlockEntity.progress > pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);
            }
        } else {
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private boolean hasRecipe(ExchangeATMBlockEntity entity) {

        if(this.mode == Mode.TO_BIGGER) {

            ItemStack result = Coins.getExchange(getItem(0), true);

            return result != null && canInsertAmountIntoOutputSlot()
                    && canInsertItemIntoOutputSlot(result)
                    && getItem(0).getCount() == 64;
        } else if(this.mode == Mode.TO_SMALLER) {

            ItemStack result = Coins.getExchange(getItem(0), false);

            return result != null && getItem(1).getCount() == 0
                    && canInsertItemIntoOutputSlot(result);
        }

        return false;
    }

    private void craftItem(ExchangeATMBlockEntity entity) {

        if(this.mode == Mode.TO_BIGGER) {
            ItemStack result = Coins.getExchange(getItem(0), true);

            if(result != null) {
                removeItem(0, 64);
                setItem(1, new ItemStack(result.getItem(),
                        getItem(1).getCount() + 1));

                entity.resetProgress();
            }

        } else if(this.mode == Mode.TO_SMALLER) {
            ItemStack result = Coins.getExchange(getItem(0), false);


            if(result != null) {
                removeItem(0, 1);
                setItem(1, result);

                entity.resetProgress();
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return getItem(1).getItem() == output.getItem() || getItem(1).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot() {
        return getItem(1).getMaxStackSize() > getItem(1).getCount();
    }

    public void updateMode() {
        if(getSpeed() >= 100) mode = Mode.TO_BIGGER;
        else if(getSpeed() <= -100) mode = Mode.TO_SMALLER;
        else mode = Mode.OFF;
    }

}