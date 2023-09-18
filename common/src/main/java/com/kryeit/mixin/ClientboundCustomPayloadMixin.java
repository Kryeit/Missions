package com.kryeit.mixin;

import com.kryeit.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundCustomPayloadPacket.class)
public abstract class ClientboundCustomPayloadMixin {
    @Shadow
    @Final
    private FriendlyByteBuf data;

    @Shadow
    @Final
    private ResourceLocation identifier;

    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
    public void handle(ClientGamePacketListener clientGamePacketListener, CallbackInfo ci) {
        if (ClientPacketHandler.handle(identifier, data)) {
            data.release();
            ci.cancel();
        }
    }
}
