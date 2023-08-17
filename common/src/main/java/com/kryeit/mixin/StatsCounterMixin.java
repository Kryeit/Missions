package com.kryeit.mixin;

import com.kryeit.missions.mission_types.StatisticMission;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsCounter.class)
public abstract class StatsCounterMixin {
    @Shadow
    public abstract <T> int getValue(StatType<T> statType, T object);

    @Inject(method = "setValue", at = @At("HEAD"))
    public void onValueChanged(Player player, Stat<?> stat, int newValue, CallbackInfo ci) {
        if (stat.getValue() instanceof ResourceLocation resourceLocation) {
            int value = getValue(Stats.CUSTOM, resourceLocation);
            StatisticMission.handleStatisticChange(player.getUUID(), newValue - value, resourceLocation);
        }
    }
}
