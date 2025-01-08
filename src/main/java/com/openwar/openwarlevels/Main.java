package com.openwar.openwarlevels;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.GUI.ItemBuilder;
import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.commands.LevelCommand;
import com.openwar.openwarlevels.commands.UnlockCommand;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.MenuHandler;
import com.openwar.openwarlevels.handlers.PlayerHandler;
import com.openwar.openwarlevels.handlers.PlayerListener;
import com.openwar.openwarlevels.level.PlayerLevel;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.UUID;

public final class Main extends JavaPlugin {
    private LevelSaveAndLoadBDD pl;
    private FactionManager fm;
    private Economy economy = null;

    private boolean setupDepend() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<LevelSaveAndLoadBDD> levelProvider = getServer().getServicesManager().getRegistration(LevelSaveAndLoadBDD.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        if (levelProvider == null || factionDataProvider == null || rsp == null) {
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        economy = rsp.getProvider();
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
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        ItemBuilder.getInstance().loadTextures(getDataFolder());
        MenuHandler menuHandler = new MenuHandler(this, null);
        LevelGUI levelGUI = new LevelGUI(pl, this, menuHandler, economy);
        menuHandler.setLevelGUI(levelGUI);
        getServer().getPluginManager().registerEvents(menuHandler, this);
        this.getCommand("level").setExecutor(new LevelCommand(pl, levelGUI));
        this.getCommand("unlock").setExecutor(new UnlockCommand(pl, levelGUI));

        getServer().getPluginManager().registerEvents(new PlayerListener(pl, fm), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, pl, fm), this);
        getServer().getPluginManager().registerEvents(new LevelLock(this, pl, fm), this);
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loaded !");
        System.out.println(" ");
        System.out.println("====================================");
        startAutoSaveTask();
    }

    public void startAutoSaveTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    UUID playerUUID = player.getUniqueId();
                    PlayerLevel data = pl.loadPlayerData(playerUUID);
                    pl.savePlayerData(playerUUID, data);
                    double rmoney = economy.getBalance(player);
                    int money = (int) Math.round(rmoney);
                    int level = data.getLevel();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setall Level: " + level + ";Money: " + money + "$;" + player.getName());
                }
            }
        }, 0L, 250L);
    }

    @Override
    public void onDisable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saving ...");
        for (Player player : getServer().getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            PlayerLevel data = pl.loadPlayerData(playerUUID);
            pl.savePlayerData(playerUUID, data);
        }
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saved !");
        System.out.println(" ");
        System.out.println("====================================");
    }
}
