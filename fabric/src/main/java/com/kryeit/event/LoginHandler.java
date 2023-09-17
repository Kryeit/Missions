package com.kryeit.event;

import com.kryeit.Main;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.entity.player.Player;

public class LoginHandler implements ServerLoginConnectionEvents.QueryStart{
    @Override
    public void onLoginStart(ServerLoginPacketListenerImpl handler, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        Player player = server.getPlayerList().getPlayerByName(handler.getUserName());
        if(player == null) return;
        Main.handlePlayerLogin(player);
    }
}
