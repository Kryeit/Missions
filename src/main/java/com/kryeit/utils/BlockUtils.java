package com.kryeit.utils;

import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class BlockUtils {

    /** Opens the exchanger menu; the network ctor reads the BlockPos that openMenu writes. */
    public static void openGui(ServerPlayer player, MechanicalExchangerBlockEntity blockEntity, BlockPos pos) {
        player.openMenu(blockEntity, pos);
    }
}
