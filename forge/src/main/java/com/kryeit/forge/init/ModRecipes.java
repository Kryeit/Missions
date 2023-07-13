package com.kryeit.forge.init;

import com.kryeit.Main;
import com.kryeit.forge.recipe.BiggerExchangeRecipe;
import com.kryeit.forge.recipe.SmallerExchangeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BiggerExchangeRecipe>> BIGGER_EXCHANGE_SERIALIZER =
            SERIALIZERS.register("bigger_exchange", BiggerExchangeRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<SmallerExchangeRecipe>> SMALLER_EXCHANGE_SERIALIZER =
            SERIALIZERS.register("smaller_exchange", SmallerExchangeRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
