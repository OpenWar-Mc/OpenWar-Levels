package com.openwar.openwarlevels.level;

import com.openwar.openwarfaction.factions.FactionManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private FileConfiguration playerDataConfig;
    private Map<UUID, PlayerLevel> playerCache = new HashMap<>();

    public PlayerDataManager() {
        loadConfig();
    }

    public PlayerLevel loadPlayerData(UUID uuid, FactionManager factionManager) {
        if (playerCache.containsKey(uuid)) {
            return playerCache.get(uuid);
        }

        int level = playerDataConfig.getInt(uuid.toString() + ".level", 0);
        double experience = playerDataConfig.getDouble(uuid.toString() + ".experience", 0);
        PlayerLevel playerLevel = new PlayerLevel(level, experience, factionManager);
        playerCache.put(uuid, playerLevel);
        return playerLevel;
    }

    public void savePlayerData(UUID uuid, PlayerLevel playerLevel) {
        playerDataConfig.set(uuid.toString() + ".level", playerLevel.getLevel());
        playerDataConfig.set(uuid.toString() + ".experience", playerLevel.getExperience());
        saveConfig();
        playerCache.put(uuid, playerLevel);
    }

    private void saveConfig() {
        try {
            playerDataConfig.save(new File("plugins/OpenWar-Levels", "players.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
    }
}
