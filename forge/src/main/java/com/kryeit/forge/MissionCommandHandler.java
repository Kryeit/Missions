package com.kryeit.forge;

import com.kryeit.Missions;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber(modid = Missions.MOD_ID)
public class MissionCommandHandler {
    @SubscribeEvent
    public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        MissionsCommand.register(dispatcher);
    }
}
