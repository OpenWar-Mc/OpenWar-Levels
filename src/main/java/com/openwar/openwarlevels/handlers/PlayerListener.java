package com.openwar.openwarlevels.handlers;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final LevelSaveAndLoadBDD levelSaveAndLoadBDD;
    private final PlayerDataManager playerDataManager;

    public PlayerListener(LevelSaveAndLoadBDD levelSaveAndLoadBDD, PlayerDataManager playerDataManager) {
        this.levelSaveAndLoadBDD = levelSaveAndLoadBDD;
        this.playerDataManager = playerDataManager;
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
        playerDataManager.setPlayerData(playerUUID);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        //récup des données depuis la bonne class et pas depuis la bdd de merde
        PlayerLevel data = playerDataManager.getPlayerData(playerUUID);
        levelSaveAndLoadBDD.savePlayerData(playerUUID, data);
        //onb l'enlève pour qu'il save pas quand le joueur est déco
        playerDataManager.removePlayerData(playerUUID);

    }
}
