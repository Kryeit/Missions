package com.kryeit.missions.missions;

import com.kryeit.missions.MissionManager;
import com.kryeit.missions.MissionType;
import com.kryeit.missions.wrappers.Player;
import com.kryeit.missions.wrappers.PlayerData;

public class VoteMission implements MissionType {
    @Override
    public String id() {
        return "vote";
    }

    @Override
    public int getProgress(Player player, String item) {
        return getData(player).getInt("votes");
    }

    @Override
    public void reset(Player player) {
        getData(player).setInt("votes", 0);
    }

    public void handleVote(Player player) {
        long currentTime = System.currentTimeMillis();
        PlayerData data = getData(player);
        if (data.getLong("last_vote") < currentTime - 86_400_000) {
            data.setLong("last_vote", currentTime);
            data.setInt("votes", 0);
        }
        data.setInt("votes", data.getInt("votes") + 1);
        MissionManager.checkReward(this, player);
    }
}
