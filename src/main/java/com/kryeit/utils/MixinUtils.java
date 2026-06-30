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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity.canCompress;

public class MixinUtils {
    // 1.21 recipes are wrapped in RecipeHolder; Create's shouldIgnoreInAutomation now takes one.
    // The basin only exposes a bare Recipe<?>, so we wrap it with a neutral (non "_manual_only") id;
    // the meaningful check (serializer AUTOMATION_IGNORE tag) reads recipe.value() only.
    private static final ResourceLocation AUTOMATION_CHECK_ID = ResourceLocation.fromNamespaceAndPath("missions", "automation_check");
    public static Player getClosestPlayer(Level level, BlockPos worldPosition) {
        if (level == null) return null;
        return level.getNearestPlayer(
                worldPosition.getX(),
                worldPosition.getY(),
                worldPosition.getZ(),
                128, false);
    }

    public static boolean isCompactingRecipe(Recipe<?> recipe) {
        return (recipe instanceof CraftingRecipe && !(recipe instanceof MechanicalCraftingRecipe) && canCompress(recipe)
                && !AllRecipeTypes.shouldIgnoreInAutomation(new RecipeHolder<>(AUTOMATION_CHECK_ID, recipe)))
               || recipe.getType() == AllRecipeTypes.COMPACTING.getType();
    }

    public static void handleMixinMissionItem(BlockEntityAccessor accessor, Class<? extends MultiResourceMissionType> missionType, ItemStack result) {
        Level level = accessor.getLevel();
        BlockPos worldPosition = accessor.getWorldPosition();
        Player closestPlayer = null;

        if (worldPosition != null)
            closestPlayer = getClosestPlayer(level, worldPosition);

        if (closestPlayer != null && result != null) {
            MissionManager.incrementMission(closestPlayer.getUUID(), missionType, BuiltInRegistries.ITEM.getKey(result.getItem()),
                    result.getCount());
        }
    }

    public static double getDistance(Vec3 pos1, Vec3 pos2) {
        if (pos1 == null || pos2 == null) return 0;
        double deltaX = pos1.x - pos2.x;
        double deltaY = pos1.y - pos2.y;
        double deltaZ = pos1.z - pos2.z;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

}
