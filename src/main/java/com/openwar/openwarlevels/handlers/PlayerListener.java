package com.openwar.openwarlevels.handlers;

import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public PlayerListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (!player.hasPlayedBefore()) {
            PlayerLevel data = new PlayerLevel(0, 0);
            playerDataManager.savePlayerData(playerUUID, data);
        } else {
            PlayerLevel data = playerDataManager.loadPlayerData(playerUUID);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        PlayerLevel data = playerDataManager.loadPlayerData(playerUUID);
        playerDataManager.savePlayerData(playerUUID, data);
    }
}