package com.openwar.openwarlevels.level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

    public PlayerLevel loadPlayerData(UUID uuid) {
        if (playerCache.containsKey(uuid)) {
            return playerCache.get(uuid);
        }

        int level = playerDataConfig.getInt(uuid.toString() + ".level", 0);
        double experience = playerDataConfig.getDouble(uuid.toString() + ".experience", 0);
        PlayerLevel playerLevel = new PlayerLevel(level, experience);
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
        File file = new File("plugins/OpenWar-Levels", "players.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(file);
    }
}
