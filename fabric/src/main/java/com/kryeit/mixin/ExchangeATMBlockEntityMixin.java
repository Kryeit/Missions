package com.kryeit.mixin;

import com.kryeit.content.atm.ExchangeATMBlockEntity;
import com.kryeit.content.atm.ExchangeATMMenu;
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

@Mixin(ExchangeATMBlockEntity.class)
public class ExchangeATMBlockEntityMixin implements ExtendedScreenHandlerFactory {

    @Unique
    ExchangeATMBlockEntity blockEntity = (ExchangeATMBlockEntity) (Object) this;

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(blockEntity.getBlockPos());
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.nullToEmpty("Exchange ATM");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return ExchangeATMMenu.create(i, inventory, blockEntity, blockEntity.data);
    }
}
