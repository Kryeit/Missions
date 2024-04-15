package com.kryeit.mixin;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.MechanicalExchangerMenu;
import com.kryeit.content.exchanger.fabric.MechanicalExchangerContainerInterface;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

@Mixin(MechanicalExchangerBlockEntity.class)
public class MechanicalExchangerBlockEntityMixin extends BlockEntity implements ExtendedScreenHandlerFactory, SidedStorageBlockEntity {

    @Unique private Storage<ItemVariant> inventoryStorage;

    @Unique
    MechanicalExchangerBlockEntity blockEntity = (MechanicalExchangerBlockEntity) (Object) this;

    public MechanicalExchangerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(blockEntity.getBlockPos());
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.missions.mechanical_exchanger");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return MechanicalExchangerMenu.create(i, inventory, blockEntity, blockEntity.data);
    }

    @Override
    @javax.annotation.Nullable
    public Storage<ItemVariant> getItemStorage(@javax.annotation.Nullable Direction face) {
        return this.inventoryStorage == null ? this.inventoryStorage = new MechanicalExchangerContainerInterface((MechanicalExchangerBlockEntity) (Object) this) : this.inventoryStorage;
    }
}
