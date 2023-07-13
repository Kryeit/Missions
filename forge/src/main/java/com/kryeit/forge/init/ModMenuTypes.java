package com.kryeit.forge.init;

import com.kryeit.forge.MainForge;
import com.kryeit.forge.screen.ExchangeATMMenu;
import com.kryeit.forge.screen.ExchangeATMScreen;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ModMenuTypes {

    public static final MenuEntry<ExchangeATMMenu> EXCHANGE_ATM_MENU =
            register("exchange_atm_menu", ExchangeATMMenu::new, () -> ExchangeATMScreen::new);



    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return MainForge.REGISTRATE
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {

    }
}
