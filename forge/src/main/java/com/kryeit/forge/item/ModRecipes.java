package com.kryeit.forge.item;

import com.kryeit.Main;
import com.kryeit.forge.recipe.ExchangeATMRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ExchangeATMRecipe>> EXCHANGE_SERIALIZER =
            SERIALIZERS.register("exchange", () -> new ExchangeATMRecipe.Serializer());

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
