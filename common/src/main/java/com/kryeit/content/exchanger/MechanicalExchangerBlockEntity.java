package com.kryeit.content.exchanger;

import com.kryeit.coins.Coins;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class MechanicalExchangerBlockEntity extends KineticBlockEntity
        implements MenuProvider, WorldlyContainer {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public NonNullList<ItemStack> inventory;

    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 64;
    Mode mode = Mode.OFF;

    public enum Mode implements StringRepresentable {
        TO_SMALLER,
        TO_BIGGER,
        OFF;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }

    public MechanicalExchangerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos worldPosition, BlockState blockState) {
        super(blockEntityType, worldPosition, blockState);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> MechanicalExchangerBlockEntity.this.progress;
                    case 1 -> MechanicalExchangerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MechanicalExchangerBlockEntity.this.progress = value;
                    case 1 -> MechanicalExchangerBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
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
        return Component.translatable("block.missions.mechanical_exchanger");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return MechanicalExchangerMenu.create(i, inventory, this, data);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.saveAllItems(tag, inventory);
        if (!clientPacket) tag.putInt("mechanical_exchanger.progress", progress);
        super.write(tag, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        ContainerHelper.loadAllItems(tag, inventory);
        if (!clientPacket) progress = tag.getInt("mechanical_exchanger.progress");
        super.read(tag, clientPacket);
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) return;

        updateMode();
        if (hasRecipe()) {
            progress++;
            setChanged();
            if (progress > maxProgress) craftItem();
        } else {
            resetProgress();
            setChanged();
        }
    }

    private boolean hasRecipe() {

        if (getItem(INPUT_SLOT).isEmpty() || !Coins.isCoin(getItem(INPUT_SLOT))) return false;

        if (this.mode == Mode.TO_BIGGER) {

            if (Coins.indexOf(getItem(INPUT_SLOT)) == Coins.getCoins().size() - 1) return false;

            ItemStack result = Coins.getExchange(getItem(INPUT_SLOT), true);

            return result != null && canInsertAmountIntoSlot(OUTPUT_SLOT, 1)
                    && canInsertItemIntoSlot(OUTPUT_SLOT, result)
                    && getItem(INPUT_SLOT).getCount() >= Coins.getCoin(getItem(INPUT_SLOT)).getCount();
        } else if (this.mode == Mode.TO_SMALLER) {

            if (Coins.indexOf(getItem(INPUT_SLOT)) == 0) return false;

            ItemStack result = Coins.getExchange(getItem(INPUT_SLOT), false);
            int requiredCount = Coins.getCoin(Coins.indexOf(getItem(INPUT_SLOT)) - 1).getCount();

            return result != null && canInsertAmountIntoSlot(OUTPUT_SLOT, requiredCount)
                    && canInsertItemIntoSlot(OUTPUT_SLOT, result)
                    && getItem(INPUT_SLOT).getCount() >= 1;
        }

        return false;
    }

    private void craftItem() {

        if (this.mode == Mode.TO_BIGGER) {
            ItemStack result = Coins.getExchange(getItem(INPUT_SLOT), true);
            if (result != null) {
                removeItem(INPUT_SLOT, Coins.getCoin(getItem(INPUT_SLOT)).getCount());
                setItem(OUTPUT_SLOT, new ItemStack(result.getItem(), getItem(OUTPUT_SLOT).getCount() + 1));

                resetProgress();
            }

        } else if (this.mode == Mode.TO_SMALLER) {
            ItemStack result = Coins.getExchange(getItem(INPUT_SLOT), false);
            if (result != null) {
                int requiredCount = Coins.getCoin(Coins.indexOf(getItem(INPUT_SLOT)) - 1).getCount();
                removeItem(INPUT_SLOT, 1);
                setItem(OUTPUT_SLOT, new ItemStack(result.getItem(), getItem(OUTPUT_SLOT).getCount() + requiredCount));
                resetProgress();
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public boolean canInsertItemIntoSlot(int slot, ItemStack output) {
        return getItem(slot).getItem() == output.getItem() || getItem(slot).isEmpty();
    }

    public boolean canInsertAmountIntoSlot(int slot, int amount) {
        return getItem(slot).getMaxStackSize() > getItem(slot).getCount() + (amount - 1);
    }

    private void updateMode() {
        double speed = getSpeed();
        this.mode = speed >= 100 ? Mode.TO_BIGGER : (speed <= -100 ? Mode.TO_SMALLER : Mode.OFF);
        updateState();
    }

    public void updateState() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.setBlock(this.worldPosition, state.setValue(MechanicalExchangerBlock.MODE, mode), 3);
        }
    }
}