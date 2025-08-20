package com.openwar.openwarlevels.handlers;

import com.openwar.openwarcore.data.LevelDataManager;
import com.openwar.openwarlevels.manager.PlayerDataManager;
import com.openwar.openwarlevels.manager.PlayerLevel;
import com.openwar.openwarlevels.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final LevelDataManager levelSaveAndLoadBDD;
    private final PlayerManager playerManager;

    public PlayerListener(LevelDataManager levelSaveAndLoadBDD, PlayerManager playerManager) {
        this.levelSaveAndLoadBDD = levelSaveAndLoadBDD;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!player.hasPlayedBefore()) {
            PlayerLevel data = new PlayerLevel(0, 0, 0, 0 ,0,0, 0);
            levelSaveAndLoadBDD.savePlayerData(playerUUID, data);
        }
        //il va load une fois les players data depuis la bdd pour permettre au autres de les utiliser
        playerManager.addPlayer(playerUUID);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        //récup des données depuis la bonne class et pas depuis la bdd de merde
        PlayerLevel data = playerManager.get(playerUUID);
        levelSaveAndLoadBDD.savePlayerData(playerUUID, data);
    }
}
