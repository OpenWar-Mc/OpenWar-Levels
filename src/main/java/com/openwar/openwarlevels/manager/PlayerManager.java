package com.openwar.openwarlevels.manager;

import com.openwar.openwarlevels.handlers.LevelLock;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerManager {

    private final PlayerDataManager dataManager;
    private final Economy economy;
    private final Plugin plugin;
    private final Map<UUID, PlayerLevel> players = new HashMap<>();

    public PlayerManager(PlayerDataManager dataManager, Economy economy, Plugin plugin) {
        this.dataManager = dataManager;
        this.economy = economy;
        this.plugin = plugin;
        loadData();
    }

    private void loadData() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            players.put(player.getUniqueId(), dataManager.load(player.getUniqueId()));
        }
    }

    public void addPlayer(UUID uuid) {
        players.put(uuid, dataManager.load(uuid));
    }

    public void removePlayer(UUID uuid) {
        PlayerLevel data = players.remove(uuid);
        if (data != null) {
            dataManager.save(uuid, data);
        }
    }

    public PlayerLevel get(UUID uuid) {
        return players.get(uuid);
    }

    public void set(UUID uuid, PlayerLevel data) {
        players.put(uuid, data);
    }
}
