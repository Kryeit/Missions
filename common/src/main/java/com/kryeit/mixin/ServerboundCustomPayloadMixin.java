package com.kryeit.mixin;

import com.kryeit.packet.ServerPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundCustomPayloadPacket.class)
public abstract class ServerboundCustomPayloadMixin {
    @Shadow
    @Final
    private FriendlyByteBuf data;

    @Shadow
    @Final
    private ResourceLocation identifier;

    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ServerGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
    public void handle(ServerGamePacketListener serverGamePacketListener, CallbackInfo ci) {
        if (!(serverGamePacketListener instanceof ServerGamePacketListenerImpl packetListener)) return;
        ServerPlayer player = packetListener.getPlayer();

        boolean handled = ServerPacketHandler.handle(identifier, player, data);
        if (handled) {
            data.release();
            ci.cancel();
        }
    }
}
