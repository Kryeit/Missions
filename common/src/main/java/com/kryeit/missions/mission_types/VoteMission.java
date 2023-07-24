package com.kryeit.missions.mission_types;

import com.kryeit.missions.MissionDifficulty;
import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class VoteMission implements MissionType {
    private static final ResourceLocation IDENTIFIER_LOCATION = new ResourceLocation("vote_mission", "vote");
    @Override
    public String id() {
        return "id";
    }

    @Override
    public MissionDifficulty difficulty() {
        return MissionDifficulty.HARD;
    }

    @Override
    public int getProgress(UUID player, ResourceLocation item) {
        return getData(player).getInt("votes");
    }

    @Override
    public void reset(UUID player) {
        getData(player).remove("votes");
    }

    public void handleVote(UUID player) {
        if (MissionManager.countItem(id(), player, IDENTIFIER_LOCATION)) {
            CompoundTag data = getData(player);
            data.putInt("votes", data.getInt("votes") + 1);

            MissionManager.checkReward(this, player, IDENTIFIER_LOCATION);
        }
    }

    @Override
    public Component description() {
        return Component.nullToEmpty("Vote mission");
    }
}
