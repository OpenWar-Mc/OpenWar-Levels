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
import org.bukkit.plugin.java.JavaPlugin;


import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private PlayerDataManager playerDataManager;
    private FactionManager fm;
    private PlayerHandler ph;
    private static final String CSV_FILE_PATH = "plugins/OpenWar-Faction/factions.csv";

    @Override
    public void onEnable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loading ...");
        this.playerDataManager = new PlayerDataManager();
        RegisteredServiceProvider<FactionManager> provider = getServer().getServicesManager().getRegistration(FactionManager.class);
        if (provider != null) {
            this.fm = provider.getProvider();
            System.out.println("FactionManager récupéré avec succès !");
        } else {
            System.out.println("Erreur : Impossible de récupérer FactionManager !");
        }
        fm.loadFactionsFromCSV(CSV_FILE_PATH);
        List<Faction> fac= fm.getAllFactions();
        System.out.println(" faction loaded: "+fac);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerDataManager), this);
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
            PlayerLevel data = playerDataManager.loadPlayerData(playerUUID);
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
