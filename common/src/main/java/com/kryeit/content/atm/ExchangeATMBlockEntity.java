package com.kryeit.content.atm;

import com.kryeit.coins.Coins;
import com.kryeit.utils.ItemHandlerCompat;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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

public class ExchangeATMBlockEntity extends KineticBlockEntity implements MenuProvider {

    private final ItemHandlerCompat itemHandler = ItemHandlerCompat.create(this, 2);

    protected Mode mode;

    enum Mode {
        TO_SMALLER, TO_BIGGER, OFF
    }


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 64;

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

    // TODO: Make getCapability in both Forge and Fabric, although it's not needed
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
//        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return lazyItemHandler.cast();
//        }
//
//        return super.getCapability(cap, side);
//    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        if (!clientPacket) {
            tag.put("inventory", itemHandler.serializeNBT());
            tag.putInt("exchange_atm.progress", progress);
        }
        super.write(tag, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        if (!clientPacket) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
            progress = tag.getInt("exchange_atm.progress");
        }
        super.read(tag, clientPacket);
    }


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
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
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        if(this.mode == Mode.TO_BIGGER) {

            ItemStack result = Coins.getExchange(inventory.getItem(0), true);

            return result != null && canInsertAmountIntoOutputSlot(inventory)
                    && canInsertItemIntoOutputSlot(inventory, result)
                    && inventory.getItem(0).getCount() == 64;
        } else if(this.mode == Mode.TO_SMALLER) {

            ItemStack result = Coins.getExchange(inventory.getItem(0), false);

            return result != null && inventory.getItem(1).getCount() == 0
                    && canInsertItemIntoOutputSlot(inventory, result);
        }

        return false;
    }

    private void craftItem(ExchangeATMBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        if(this.mode == Mode.TO_BIGGER) {
            ItemStack result = Coins.getExchange(inventory.getItem(0), true);

            if(result != null) {
                entity.itemHandler.extractItem(0,64, false);
                entity.itemHandler.setStackInSlot(1, new ItemStack(result.getItem(),
                        entity.itemHandler.getStackInSlot(1).getCount() + 1));

                entity.resetProgress();
            }

        } else if(this.mode == Mode.TO_SMALLER) {
            ItemStack result = Coins.getExchange(inventory.getItem(0), false);


            if(result != null) {
                entity.itemHandler.extractItem(0,1, false);
                entity.itemHandler.setStackInSlot(1, new ItemStack(result.getItem(),
                        64));

                entity.resetProgress();
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(1).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }

    public void updateMode() {
        if(getSpeed() >= 100) mode = Mode.TO_BIGGER;
        else if(getSpeed() <= -100) mode = Mode.TO_SMALLER;
        else mode = Mode.OFF;
    }

    public ItemHandlerCompat getItemHandler() {
        return itemHandler;
    }

}