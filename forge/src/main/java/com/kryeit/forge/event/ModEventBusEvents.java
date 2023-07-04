package com.kryeit.forge.event;

import com.kryeit.Main;
import com.kryeit.forge.recipe.ExchangeATMRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(ExchangeATMRecipe.Serializer.INSTANCE.setRegistryName(ExchangeATMRecipe.Serializer.ID));
    }
}
