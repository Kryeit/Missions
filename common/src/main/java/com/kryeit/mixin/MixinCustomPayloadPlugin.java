package com.kryeit.mixin;

import com.kryeit.client.ClientsideActiveMission;
import com.kryeit.client.ClientsideMissionPacketUtils;
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

import java.util.List;

@Mixin(ClientboundCustomPayloadPacket.class)
public abstract class MixinCustomPayloadPlugin {
    @Shadow @Final private FriendlyByteBuf data;

    @Shadow @Final private ResourceLocation identifier;

    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
    public void handle(ClientGamePacketListener clientGamePacketListener, CallbackInfo ci) {
        if (!identifier.equals(ClientsideMissionPacketUtils.IDENTIFIER)) return;

        List<ClientsideActiveMission> missions = ClientsideMissionPacketUtils.deserialize(data);
        ClientsideMissionPacketUtils.setMissions(missions);
        System.out.println("Received missions: " + missions);

        ci.cancel();
    }
}
