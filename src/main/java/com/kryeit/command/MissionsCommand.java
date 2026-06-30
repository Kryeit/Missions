package com.kryeit.command;

import com.kryeit.network.MissionPayloads;
import com.kryeit.utils.Lang;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class MissionsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("missions")
                .executes(ctx -> openMissions(ctx.getSource())));
    }

    private static int openMissions(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Lang.t("missions.command.players_only", "Only players can open this menu."));
            return 0;
        }
        // Tell the client to open the custom screen; the screen requests its data on init.
        PacketDistributor.sendToPlayer(player, new MissionPayloads.OpenMissions());
        return Command.SINGLE_SUCCESS;
    }
}
