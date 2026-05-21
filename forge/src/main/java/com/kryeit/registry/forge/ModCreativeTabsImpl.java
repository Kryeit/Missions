package com.kryeit.registry.forge;

import com.kryeit.Missions;
import com.kryeit.registry.ModCreativeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.kryeit.Missions.REGISTRATE;
import static com.kryeit.registry.ModBlocks.MECHANICAL_EXCHANGER;

@EventBusSubscriber()
public class ModCreativeTabsImpl {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Missions.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Create: Missions"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(MECHANICAL_EXCHANGER::asStack)
                    .displayItems(new ModCreativeTabs.RegistrateDisplayItemsGenerator(ModCreativeTabs.Tabs.MAIN))
                    .build());

    public static <T> T getNonNullSupplierValue(RegistryEntry<T, ?> a) {
        return a.get();
    }

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

    public static void acceptOutput(CreativeModeTab.Output output, ItemStack item, boolean parentOnly) {
        output.accept(item, parentOnly ? TabVisibility.PARENT_TAB_ONLY : TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
