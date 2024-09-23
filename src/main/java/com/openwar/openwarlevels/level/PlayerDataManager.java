package com.openwar.openwarlevels.level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private File file;
    private FileConfiguration playerDataConfig;

    public PlayerDataManager() {
        file = new File("plugins/OpenWar-Levels", "players.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayerData(UUID uuid, PlayerLevel data) {
        playerDataConfig.set(uuid.toString() + ".level", data.getLevel());
        playerDataConfig.set(uuid.toString() + ".experience", data.getExperience());
        saveConfig();
    }

    public PlayerLevel loadPlayerData(UUID uuid) {
        int level = playerDataConfig.getInt(uuid.toString() + ".level", 0);
        int experience = playerDataConfig.getInt(uuid.toString() + ".experience", 0);
        return new PlayerLevel(level, experience);
    }

    private void saveConfig() {
        try {
            playerDataConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
