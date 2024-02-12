package com.kryeit.missions.mission_types.create.train;

import com.kryeit.JSONObject;
import com.kryeit.Missions;
import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.simibubi.create.AllBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class TrainDriverPassengerMissionType implements MissionType {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation(Missions.MOD_ID, "distance");
    private static final ResourceLocation PASSENGERS = new ResourceLocation(Missions.MOD_ID, "passengers");

    public static void handleDistanceChange(UUID player, int difference) {
        MissionManager.incrementMission(player, TrainDriverPassengerMissionType.class, IDENTIFIER, difference);
    }

    public static int passengersNeeded() {
        int passengers = 1;
        try {
            File file = Path.of("missions").toFile();
            if (!file.exists()) return passengers;
            String jsonContent = new String(Files.readAllBytes(Paths.get("missions/missions.json")));
            JSONObject object = new JSONObject(jsonContent);
            JSONObject thing = object.getObject("train-driver-passenger");
            if (thing != null) {
                passengers = Integer.parseInt(thing.getObject("requirements").getString(PASSENGERS.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Math.max(passengers, 1);
    }

    @Override
    public String id() {
        return "train-driver-passenger";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Train passenger mission");
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt("value") / 1_000;
    }

    @Override
    public void reset(UUID player) {
        getData(player).remove("value");
    }

    @Override
    public void increment(int amount, ResourceLocation item, CompoundTag data) {
        data.putInt("value", data.getInt("value") + amount);
    }

    @Override
    public ItemStack getItemStack(ResourceLocation item) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public ItemStack getPreviewStack(ResourceLocation item) {
        return AllBlocks.TRACK_STATION.asStack();
    }

}
