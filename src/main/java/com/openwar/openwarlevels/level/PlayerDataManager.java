package com.openwar.openwarlevels.level;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarlevels.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager {

    LevelSaveAndLoadBDD levelSaveAndLoadBDD;
    Main main;
    Economy economy;


    HashMap<UUID, PlayerLevel> playerData = new HashMap<>();

    public PlayerDataManager(LevelSaveAndLoadBDD levelSaveAndLoadBDD, Main main, Economy economy) {
        this.levelSaveAndLoadBDD = levelSaveAndLoadBDD;
        this.economy = economy;
        this.main = main;
        startAutoSaveTask();
    }

    public void setPlayerData(UUID uuid) {
        playerData.put(uuid, levelSaveAndLoadBDD.loadPlayerData(uuid));
    }

    public PlayerLevel getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public void removePlayerData(UUID uuid) {
        playerData.remove(uuid);
    }

    public void startAutoSaveTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player player : players) {
                    UUID playerUUID = player.getUniqueId();
                    //On get la data depuis ce gestionnaire pas depuis la bdd
                    PlayerLevel data = playerData.get(playerUUID);
                    //on save les données dans la bdd
                    levelSaveAndLoadBDD.savePlayerData(playerUUID, data);
                    //on affiche ça dans le haut de l'écran hud du joueur zby
                    double rmoney = economy.getBalance(player);
                    int money = (int) Math.round(rmoney);
                    int level = data.getLevel();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setall Level: " + level + ";Money: " + money + "$;" + player.getName());
                }
            }
        }, 0L, 250L);
    }
}
