package com.openwar.openwarlevels;

import com.openwar.openwarcore.data.LevelDataManager;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.GUI.ItemBuilder;
import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.commands.LevelCommand;
import com.openwar.openwarlevels.commands.UnlockCommand;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.MenuHandler;
import com.openwar.openwarlevels.handlers.PlayerHandler;
import com.openwar.openwarlevels.handlers.PlayerListener;
import com.openwar.openwarlevels.manager.PlayerDataManager;
import com.openwar.openwarlevels.manager.PlayerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.UUID;

public final class Main extends JavaPlugin {
    private LevelDataManager pl;
    private FactionManager fm;
    private PlayerManager pm;
    private PlayerDataManager pld;
    private Economy economy = null;

    private boolean setupDepend() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<LevelDataManager> levelProvider = getServer().getServicesManager().getRegistration(LevelDataManager.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        RegisteredServiceProvider<PlayerManager> playerDataProvider = getServer().getServicesManager().getRegistration(PlayerManager.class);
        if (levelProvider == null || factionDataProvider == null || rsp == null || playerDataProvider == null) {
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!");
            if (levelProvider == null) {
                System.out.println("Level Provider");
            }
            if (factionDataProvider == null) {
                System.out.println("Faction Manager");
            }
            if (rsp == null) {
                System.out.println("Level Save and Load BDD");
            }
            if (playerDataProvider == null) {
                System.out.println("Player Manager");
            }
            return false;
        }
        economy = rsp.getProvider();
        pl = levelProvider.getProvider();
        fm = factionDataProvider.getProvider();
        pm = playerDataProvider.getProvider();
        return true;
    }

    @Override
    public void onEnable() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, loading ...");
        if (!setupDepend()) {
            return;
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        ItemBuilder.getInstance().loadTextures(getDataFolder());
        pld = new PlayerDataManager(pl);

        MenuHandler menuHandler = new MenuHandler(this, null);
        LevelGUI levelGUI = new LevelGUI(pl, pm, this, menuHandler, economy);
        menuHandler.setLevelGUI(levelGUI);

        getServer().getPluginManager().registerEvents(new LevelLock(this, fm, pm), this);
        getServer().getPluginManager().registerEvents(menuHandler, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(pl, pm), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this, fm, pm), this);

        this.getCommand("level").setExecutor(new LevelCommand(levelGUI));
        this.getCommand("unlock").setExecutor(new UnlockCommand(levelGUI));

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
            pld.save(playerUUID, pm.get(playerUUID));
        }
        System.out.println(" ");
        System.out.println(" OpenWar - Levels, Saved !");
        System.out.println(" ");
        System.out.println("====================================");
    }
}
