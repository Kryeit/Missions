package com.kryeit.mixin;

import com.kryeit.packet.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomPacketPayload.class)
public abstract class CustomPacketPayloadMixin {
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
