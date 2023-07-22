package com.kryeit;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class Utils {
    public static int getDay() {
        return (int) (System.currentTimeMillis() / 86_400_000);
    }

    /**
     * @return THe current day of the week. 0 is monday.
     */
    public static int getDayOfWeek() {
        return (getDay() + 3) % 7;
    }

    public static ItemStack getItem(ResourceLocation item) {
        return Registry.ITEM.get(item).getDefaultInstance();
    }
}
