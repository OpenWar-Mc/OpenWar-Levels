package com.openwar.openwarlevels;

import com.openwar.openwarfaction.commands.AdminCommand;
import com.openwar.openwarfaction.factions.FactionGUI;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.commands.LevelAdminCommand;
import com.openwar.openwarlevels.commands.LevelCommand;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.MenuHandler;
import com.openwar.openwarlevels.handlers.PlayerHandler;
import com.openwar.openwarlevels.handlers.PlayerListener;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.UUID;

public final class Main extends JavaPlugin {
    private PlayerDataManager pl;
    private FactionManager fm;
    private PlayerHandler ph;
    private LevelGUI gui;
    private LevelLock lock;

    private boolean setupDepend() {
        RegisteredServiceProvider<PlayerDataManager> levelProvider = getServer().getServicesManager().getRegistration(PlayerDataManager.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        if (levelProvider == null || factionDataProvider == null) {
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        pl = levelProvider.getProvider();
        fm = factionDataProvider.getProvider();
        return true;
    }

    @Override
    public void onEnable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loading ...");
        if (!setupDepend()) {return;}
        gui = new LevelGUI(pl);
        getServer().getPluginManager().registerEvents(new PlayerListener(pl, fm), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, pl, fm), this);
        getServer().getPluginManager().registerEvents(new LevelLock(this, pl, fm), this);
        getServer().getPluginManager().registerEvents(new MenuHandler(pl, gui), this);

        this.getCommand("leveladmin").setExecutor(new LevelAdminCommand(pl, this));
        this.getCommand("level").setExecutor(new LevelCommand(pl, gui));
        gui.generateLeaderboardCache();
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loaded !");
        System.out.println(" ");
        System.out.println("====================================");
        new BukkitRunnable() {
            @Override
            public void run() {
                gui.checkForLeaderboardUpdates();
            }
        }.runTaskTimer(this, 0, 3600);


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
}
