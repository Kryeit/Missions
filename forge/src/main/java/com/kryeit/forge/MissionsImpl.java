package com.kryeit.forge;

import com.kryeit.Missions;
import com.kryeit.content.exchanger.MechanicalExchangerBlockEntity;
import com.kryeit.content.exchanger.forge.MechanicalExchangerContainerInterface;
import com.kryeit.multiloader.Env;
import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModStats;
import com.kryeit.registry.forge.ModCreativeTabsImpl;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.kryeit.Missions.REGISTRATE;

@Mod(Missions.MOD_ID)
@EventBusSubscriber
public class MissionsImpl {
    private static IEventBus modBus;

    public MissionsImpl(IEventBus modBus) {
        MissionsImpl.modBus = modBus;
        Missions.init();

        ModCreativeTabsImpl.register(modBus);

        MissionsImpl.modBus.addListener(this::onConfigRead);

        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
        forgeEventBus.register(new MissionHandler());

        forgeEventBus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            Player player = event.getEntity();
            Missions.handlePlayerLogin(player);
        });

        Env.CLIENT.runIfCurrent(() -> MissionsClientImpl::init);

    }

    @SubscribeEvent  // on the mod event bus
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) ->
                        new MechanicalExchangerContainerInterface((MechanicalExchangerBlockEntity) be)
                ,
                // blocks to register for
                ModBlocks.MECHANICAL_EXCHANGER.get()
        );
    }

    public static void finalizeRegistrate() {
        REGISTRATE.registerEventListeners(modBus);
    }

    private void onConfigRead(FMLCommonSetupEvent event) {
        Missions.readConfig();
        ModStats.register();
    }
}
