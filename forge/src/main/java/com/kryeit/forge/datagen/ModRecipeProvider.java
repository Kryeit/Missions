package com.kryeit.forge.datagen;

import com.kryeit.forge.datagen.custom.ExchangeATMRecipeBuilder;
import com.kryeit.item.Coins;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {

        new ExchangeATMRecipeBuilder(Coins.goldCoin(), Coins.copperCoin(), 64)
                .save(pFinishedRecipeConsumer);

        new ExchangeATMRecipeBuilder(Coins.copperCoin(), Coins.ironCoin(), 64)
                .save(pFinishedRecipeConsumer);
    }
}
