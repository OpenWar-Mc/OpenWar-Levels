package com.openwar.openwarlevels;

import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.PlayerHandler;
import com.openwar.openwarlevels.handlers.PlayerListener;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.UUID;

public final class Main extends JavaPlugin {
    private PlayerDataManager pl;
    private FactionManager fm;
    private PlayerHandler ph;

    @Override
    public void onEnable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loading ...");

        RegisteredServiceProvider<PlayerDataManager> levelProvider = getServer().getServicesManager().getRegistration(PlayerDataManager.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        pl = levelProvider.getProvider();
        fm = factionDataProvider.getProvider();



        getServer().getPluginManager().registerEvents(new PlayerListener(pl, fm), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, pl, fm), this);
        getServer().getPluginManager().registerEvents(new LevelLock(this, pl, fm), this);
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
            PlayerLevel data = pl.loadPlayerData(playerUUID, fm);
            pl.savePlayerData(playerUUID, data);
        }
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saved !");
        System.out.println(" ");
        System.out.println("====================================");
    }

    public PlayerDataManager getPlayerDataManager() {
        return pl;
    }
}
