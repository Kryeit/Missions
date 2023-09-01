package com.kryeit.mixin;

import com.kryeit.content.atm.ExchangeATMBlockEntity;
import com.kryeit.content.atm.ExchangeATMMenu;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(ExchangeATMBlockEntity.class)
public class ExchangeATMBlockEntityMixin implements ExtendedScreenHandlerFactory {

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;
        buf.writeBlockPos(accessor.getWorldPosition());
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.nullToEmpty("Exchange ATM");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        BlockEntityAccessor accessor = (BlockEntityAccessor) this;
        ExchangeATMBlockEntity atmBlockEntity = (ExchangeATMBlockEntity) accessor.getLevel().getBlockEntity(accessor.getWorldPosition());
        return ExchangeATMMenu.create(i, inventory, atmBlockEntity, atmBlockEntity.data);
    }
}
