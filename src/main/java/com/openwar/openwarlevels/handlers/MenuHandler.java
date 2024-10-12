package com.openwar.openwarlevels.handlers;

import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuHandler implements Listener {
    private PlayerDataManager playerDataManager;
    private LevelGUI gui;
    private Map<UUID, Integer> playerPages;
    private Map<UUID, Integer> leaderPages;

    public MenuHandler(PlayerDataManager playerDataManager, LevelGUI gui) {
        this.playerDataManager = playerDataManager;
        this.gui = gui;
        this.playerPages = new HashMap<>();
        this.leaderPages = new HashMap<>();
    }




    public int getCurrentPage(UUID playerUUID, String gui) {
        if (gui.equals("unlock")) {
            return playerPages.getOrDefault(playerUUID, 1);
        }
        if (gui.equals("leader")) {
            return leaderPages.getOrDefault(playerUUID, 1);
        }
        return 0;
    }

    public void setCurrentPage(UUID playerUUID, int page, String gui) {
        if (gui.equals("unlock")) {
            playerPages.put(playerUUID, page);
        }
        if (gui.equals("leader")) {
            leaderPages.put(playerUUID, page);
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView view = event.getView();
        UUID playerUUID = player.getUniqueId();

        if (view.getTitle().contains("§8§k§l!!§r §c§lLevel §f- §c§lGUI §8§k§l!!")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 13) {
                gui.openLeaderBoardPage(player);
            }
            if (slot == 15) {
                gui.openUnlockPage(player, 1, gui.getTotalPages("unlock"), gui.getLockList(), gui.getPlayerLevel(player.getUniqueId()));
            }
        }

        if (view.getTitle().startsWith("§8§k§l!!§r §4§lUnlock §f- §4§l")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 48) {
                int currentPage = getCurrentPage(playerUUID, "unlock");
                System.out.println("Current page: "+currentPage);
                if (currentPage > 1) {
                    setCurrentPage(playerUUID, currentPage - 1,"unlock");
                    gui.openUnlockPage(player, currentPage - 1, gui.getTotalPages("unlock"), gui.getLockList(), gui.getPlayerLevel(playerUUID));
                }
            } else if (slot == 50) {
                int currentPage = getCurrentPage(playerUUID, "unlock");
                System.out.println("Current page: "+currentPage);
                if (currentPage < gui.getTotalPages("unlock")) {
                    setCurrentPage(playerUUID, currentPage + 1,"unlock");
                    gui.openUnlockPage(player, currentPage + 1, gui.getTotalPages("unlock"), gui.getLockList(), gui.getPlayerLevel(playerUUID));
                }
            }
        }

        if (view.getTitle().startsWith("§8§k§l!!§r §4§lLeaderBoard §8§k§l!!§r")) {
            event.setCancelled(true);
        }
    }
}
