package com.kryeit.mixin;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.MechanicalExchangerMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

@Mixin(MechanicalExchangerBlockEntity.class)
public class ExchangeATMBlockEntityMixin implements ExtendedScreenHandlerFactory {

    @Unique
    MechanicalExchangerBlockEntity blockEntity = (MechanicalExchangerBlockEntity) (Object) this;

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(blockEntity.getBlockPos());
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.missions.exchange_atm");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return MechanicalExchangerMenu.create(i, inventory, blockEntity, blockEntity.data);
    }
}
