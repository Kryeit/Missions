package com.kryeit.registry;

import com.kryeit.Main;
import com.kryeit.content.exchanger.MechanicalExchangerMenu;
import com.kryeit.content.exchanger.MechanicalExchangerScreen;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ModMenuTypes {

    public static final MenuEntry<MechanicalExchangerMenu> MECHANICAL_EXCHANGER_MENU =
            register("mechanical_exchanger_menu", MechanicalExchangerMenu::new, () -> MechanicalExchangerScreen::new);


    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
            String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return Main.registrate()
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {

    }
}
