package com.kryeit.utils;

import com.kryeit.content.atm.ExchangeATMBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class BlockUtils {

    @ExpectPlatform
    public static void openGui(ServerPlayer player, ExchangeATMBlockEntity blockEntity, BlockPos pos) {
        throw new AssertionError();
    }
}
