package com.openwar.openwarlevels.handlers;

import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class MenuHandler implements Listener {
    private PlayerDataManager playerDataManager;
    private  LevelGUI gui;

    public MenuHandler(PlayerDataManager playerDataManager, LevelGUI gui) {
        this.playerDataManager = playerDataManager;
        this.gui = gui;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView view = event.getView();
        Inventory topInventory = view.getTopInventory();

        if (view.getTitle().contains("§8§k§l!!§r §c§lLevel §f- §c§lGUI §8§k§l!!")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 11) {

            }
            if (slot == 13) {

            }
            if (slot == 15) {

            }
        }
    }
}
