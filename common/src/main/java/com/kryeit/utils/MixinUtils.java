package com.kryeit.utils;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import static com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity.canCompress;

public class MixinUtils {
    public static Player getClosestPlayer(Level level, BlockPos worldPosition) {
        return level.getNearestPlayer(
                worldPosition.getX(),
                worldPosition.getY(),
                worldPosition.getZ(),
                128, false);
    }

    public static boolean isCompactingRecipe(Recipe<?> recipe) {
        return (recipe instanceof CraftingRecipe && !(recipe instanceof MechanicalCraftingRecipe) && canCompress(recipe)
                && !AllRecipeTypes.shouldIgnoreInAutomation(recipe))
               || recipe.getType() == AllRecipeTypes.COMPACTING.getType();
    }

    public static void handleMixinMissionItem(BlockEntityAccessor accessor, Class<? extends MultiResourceMissionType> missionType, ItemStack result) {
        Level level = accessor.getLevel();
        BlockPos worldPosition = accessor.getWorldPosition();
        Player closestPlayer = null;

        if (level != null && worldPosition != null)
            closestPlayer = getClosestPlayer(level, worldPosition);

        if (closestPlayer != null && result != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), missionType, Registry.ITEM.getKey(result.getItem()),
                    result.getCount());
        }
    }
}
