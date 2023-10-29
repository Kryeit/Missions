package com.kryeit.utils;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.mission_types.MultiResourceMissionType;
import com.kryeit.mixin.interfaces.BlockEntityAccessor;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
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
            MissionManager.incrementMission(closestPlayer.getUUID(), missionType, BuiltInRegistries.ITEM.getKey(result.getItem()),
                    result.getCount());
        }
    }

    public static double getDistance(BlockPos pos1, BlockPos pos2) {
        double deltaX = pos1.getX() - pos2.getX();
        double deltaY = pos1.getY() - pos2.getY();
        double deltaZ = pos1.getZ() - pos2.getZ();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
}
