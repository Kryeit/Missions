package com.kryeit.missions;

import com.kryeit.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface MissionType {
    String id();

    boolean isLonely();

    MissionDifficulty difficulty();

    Component description();

    int getProgress(UUID player, ResourceLocation item);

    void reset(UUID player, ResourceLocation item);

    default CompoundTag getData(UUID player) {
        return MissionManager.getStorage().getMissionData(id(), player);
    }

    default ItemStack getItemStack(ResourceLocation item) {
        return Utils.getItem(item);
    }

    default ItemStack getPreviewStack(ResourceLocation item) {
        return getItemStack(item);
    }

    void increment(int amount, ResourceLocation item, CompoundTag data);
}
