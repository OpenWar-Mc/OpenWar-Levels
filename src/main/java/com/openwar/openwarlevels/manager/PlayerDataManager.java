package com.openwar.openwarlevels.manager;

import com.openwar.openwarcore.data.LevelDataManager;
import java.util.UUID;

public class PlayerDataManager {

    private final LevelDataManager bdd;

    public PlayerDataManager(LevelDataManager bdd) {
        this.bdd = bdd;
    }

    public PlayerLevel load(UUID uuid) {
        return bdd.loadPlayerData(uuid);
    }

    public void save(UUID uuid, PlayerLevel data) {
        bdd.savePlayerData(uuid, data);
    }
}



