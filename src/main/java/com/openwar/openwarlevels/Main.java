package com.openwar.openwarlevels;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.PlayerHandler;
import com.openwar.openwarlevels.handlers.PlayerListener;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private PlayerDataManager playerDataManager;
    private FactionManager fm;
    private PlayerHandler ph;

    @Override
    public void onEnable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loading ...");
        this.playerDataManager = new PlayerDataManager();
        RegisteredServiceProvider<FactionManager> factionProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        FactionManager factionManager = factionProvider.getProvider();
        getServer().getPluginManager().registerEvents(new PlayerListener(playerDataManager, fm), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, playerDataManager, fm), this);
        getServer().getPluginManager().registerEvents(new LevelLock(this, playerDataManager, fm), this);
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loaded !");
        System.out.println(" ");
        System.out.println("====================================");

    }

    @Override
    public void onDisable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saving ...");
        for (Player player : getServer().getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            PlayerLevel data = playerDataManager.loadPlayerData(playerUUID, fm);
            playerDataManager.savePlayerData(playerUUID, data);
        }
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saved !");
        System.out.println(" ");
        System.out.println("====================================");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
