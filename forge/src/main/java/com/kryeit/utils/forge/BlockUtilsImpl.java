package com.kryeit.utils.forge;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class BlockUtilsImpl {
    public static void openGui(ServerPlayer player, MechanicalExchangerBlockEntity blockEntity, BlockPos pos) {
        NetworkHooks.openScreen(player, blockEntity, pos);
    }
}
