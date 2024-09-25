package com.openwar.openwarlevels.GUI;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class LevelGUI {

    private PlayerDataManager playerDataManager;

    public LevelGUI(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    private void addBorders(Inventory inv, int rows) {
        int size = rows * 9;
        for (int i = 0; i < size; i++) {
            if (i < 9 || (i % 9 == 0) || (i % 9 == 8) || (i >= size - 9)) {
                ItemStack pane = createColoredGlassPane(Material.STAINED_GLASS_PANE, (short) 15, " ");
                inv.setItem(i, pane);
            }
        }
    }
    private ItemStack createColoredGlassPane(Material material, short data, String name) {
        ItemStack glassPane = new ItemStack(material, 1, data);
        ItemMeta meta = glassPane.getItemMeta();
        meta.setDisplayName(name);
        glassPane.setItemMeta(meta);
        return glassPane;
    }

    //=========================================== OPEN LEVEL GUI TO PLAYER=======================================

    public void openLevelGUI(Player player) {
        UUID playerUUID = player.getUniqueId();
        Inventory menu = Bukkit.createInventory(null, 36, "§c§lLevel §f - §4" + player.getName());
        addBorders(menu, 4);

        //TODO LEADERBOARD, HEAD OF PLAYER INFO, UNLOCK MENU


        //menu.setItem(24, factionLevelItem);
        //menu.setItem(30, infoItem);
        //menu.setItem(32, upgradeItem);
        //menu.setItem(20, leaderHead);
        //menu.setItem(22, fperm);
//
        player.openInventory(menu);
    }



}
