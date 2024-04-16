package com.kryeit.forge;

import com.kryeit.Missions;
import com.kryeit.registry.forge.KeyInit;
import com.kryeit.registry.forge.ModCreativeTabsImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.kryeit.Missions.REGISTRATE;

@Mod(Missions.MOD_ID)
@Mod.EventBusSubscriber
public class MissionsImpl {

    static IEventBus bus;

    public MissionsImpl() {
        bus = FMLJavaModLoadingContext.get().getModEventBus();
        Missions.init();

        ModCreativeTabsImpl.register(bus);

        bus.addListener(this::doClientStuff);
        bus.addListener(this::onConfigRead);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(new MissionHandler());

        forgeEventBus.addListener((PlayerLoggedInEvent event) -> {
            Player player = event.getEntity();
            Missions.handlePlayerLogin(player);
        });
    }

    public static void finalizeRegistrate() {
        REGISTRATE.registerEventListeners(bus);
    }

    private void onConfigRead(final FMLCommonSetupEvent event) {
        Missions.readConfig();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInit());
    }
}
