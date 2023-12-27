package com.kryeit.forge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class MissionsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("missions").executes(MissionsCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> command){
        return Command.SINGLE_SUCCESS;
    }
}
