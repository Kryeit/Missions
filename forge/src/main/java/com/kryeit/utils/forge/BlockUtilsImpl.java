package com.kryeit.utils.forge;

import com.kryeit.content.atm.ExchangeATMBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkHooks;

public class BlockUtilsImpl {
    public static void openGui(ServerPlayer player, ExchangeATMBlockEntity blockEntity, BlockPos pos) {
        NetworkHooks.openScreen(player, blockEntity, pos);
    }
}
