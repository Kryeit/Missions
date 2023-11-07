package com.kryeit.content.atm;

import com.kryeit.coins.Coins;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.kryeit.coins.Coins.EXCHANGE_RATE;

public class ExchangeATMBlockEntity extends KineticBlockEntity
        implements MenuProvider, WorldlyContainer {

    public NonNullList<ItemStack> inventory;

    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 64;

    Mode mode = Mode.OFF;

    enum Mode {
        TO_SMALLER,
        TO_BIGGER,
        OFF
    }

    public ExchangeATMBlockEntity(BlockEntityType<?> blockEntityType, BlockPos worldPosition, BlockState blockState) {
        super(blockEntityType, worldPosition, blockState);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
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

    @Override
    public int [] getSlotsForFace(Direction direction) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
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
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.missions.exchange_atm");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return ExchangeATMMenu.create(i, inventory, this, data);
    }

//    @Override
//    public void onLoad() {
//        super.onLoad();
//    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.saveAllItems(tag, inventory);
        if (!clientPacket) tag.putInt("exchange_atm.progress", progress);
        super.write(tag, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.loadAllItems(tag, inventory);
        if (!clientPacket) progress = tag.getInt("exchange_atm.progress");
        super.read(tag, clientPacket);
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this);
    }

    public void tick(Level level, BlockPos pos, BlockState state, ExchangeATMBlockEntity blockEntity) {
        updateMode();
        if (hasRecipe()) {
            progress++;
            setChanged(level, pos, state);
            if (progress > maxProgress) craftItem();
        } else {
            resetProgress();
            setChanged(level, pos, state);
        }
    }

    private boolean hasRecipe() {

        if(this.mode == Mode.TO_BIGGER) {

            ItemStack result = Coins.getExchange(getItem(0), true);

            return result != null && canInsertAmountIntoOutputSlot(1)
                    && canInsertItemIntoOutputSlot(result)
                    && getItem(0).getCount() >= EXCHANGE_RATE;
        } else if(this.mode == Mode.TO_SMALLER) {

            ItemStack result = Coins.getExchange(getItem(0), false);

            return result != null && canInsertAmountIntoOutputSlot(result.getCount())
                    && canInsertItemIntoOutputSlot(result);
        }

        return false;
    }

    private void craftItem() {

        if(this.mode == Mode.TO_BIGGER) {
            ItemStack result = Coins.getExchange(getItem(0), true);
            if(result != null) {
                removeItem(0, EXCHANGE_RATE);
                setItem(1, new ItemStack(result.getItem(), getItem(1).getCount() + 1));

                resetProgress();
            }

        } else if(this.mode == Mode.TO_SMALLER) {
            ItemStack result = Coins.getExchange(getItem(0), false);
            if(result != null) {
                removeItem(0, 1);
                setItem(1, result);

                resetProgress();
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return getItem(1).getItem() == output.getItem() || getItem(1).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(int amount) {
        return getItem(1).getMaxStackSize() > getItem(1).getCount() + (amount - 1);
    }

    public void updateMode() {
        if(getSpeed() >= 100) mode = Mode.TO_BIGGER;
        else if(getSpeed() <= -100) mode = Mode.TO_SMALLER;
        else mode = Mode.OFF;
    }

}