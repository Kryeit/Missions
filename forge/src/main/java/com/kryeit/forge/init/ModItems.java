package com.kryeit.forge.init;

import com.kryeit.Main;
import com.kryeit.forge.item.ExchangeATMItem;
import com.kryeit.forge.tab.CreativeModeTabs;
import com.kryeit.forge.tab.MissionsCreativeModeTab;
import com.kryeit.forge.tab.MissionsCreativeTabs;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.simibubi.create.Create.REGISTRATE;

public class ModItems {
    static {
        REGISTRATE.creativeModeTab(() -> CreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static void register() {}
}
