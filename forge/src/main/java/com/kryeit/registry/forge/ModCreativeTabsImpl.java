package com.kryeit.registry.forge;

import com.kryeit.Missions;
import com.kryeit.registry.ModCreativeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.kryeit.Missions.REGISTRATE;
import static com.kryeit.registry.ModBlocks.MECHANICAL_EXCHANGER;

@EventBusSubscriber(bus = Bus.MOD)
public class ModCreativeTabsImpl {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Missions.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Create: Missions"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> MECHANICAL_EXCHANGER.asStack())
                    .displayItems(new ModCreativeTabs.RegistrateDisplayItemsGenerator(ModCreativeTabs.Tabs.MAIN))
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.getKey();
    }

    public static void useBaseTab() {
        REGISTRATE.setCreativeTab(MAIN_TAB);
    }
}
