package com.kryeit.missions.handlers;

import com.kryeit.missions.MissionTypeRegistry;
import com.kryeit.missions.missions.VoteMission;
import com.kryeit.missions.wrappers.Player;

public class VoteHandler {
    // this is an example implementation of a mission
    public void onVote(Player player) {
        ((VoteMission) MissionTypeRegistry.INSTANCE.getType("vote")).handleVote(player);
    }
}
