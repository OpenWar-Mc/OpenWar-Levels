package com.openwar.openwarlevels.GUI;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
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
            }  else {
                ItemStack pane = createColoredGlassPane(Material.STAINED_GLASS_PANE, (short) 0, " ");
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
        Inventory menu = Bukkit.createInventory(null, 27, "§c§lLevel §f - §4" + player.getName());
        addBorders(menu, 3);

        //TODO LEADERBOARD, HEAD OF PLAYER INFO, UNLOCK MENU

        menu.setItem(11, getPlayerHeadInfo(player.getName()));
        menu.setItem(13, getLeaderboard());
        menu.setItem(15, getUnlock());

        player.openInventory(menu);
    }



    public ItemStack getPlayerHeadInfo(String playerName) {
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        if (meta != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            PlayerLevel playerLevel = playerDataManager.loadPlayerData(player.getUniqueId(), null);

            int level = playerLevel.getLevel();
            double xp = playerLevel.getExperience();
            double currentLevelXp = playerLevel.getExpCurrentLevel();
            double nextLevelXp = playerLevel.getExpNextLevel();
            double percent = ((xp - currentLevelXp) / (nextLevelXp - currentLevelXp)) * 100;

            int progress = (int) ((percent / 100) * 10);

            meta.setOwningPlayer(player);
            meta.setDisplayName("§4§l" + playerName);
            meta.setLore(Arrays.asList(
                    "§7Level §8: §c" + level,
                    "§7exp §8: §c"+xp+"§8/§c"+nextLevelXp,
                    "§7Progression §8: " + getProgressBar(progress, 10) + " §c" + String.format("%.2f", percent) + "%"
            ));
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }


    public ItemStack getIconItem(Material material, String name, String lore1, String lore2, String lore3) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore1, lore2, lore3));
        }
        return item;
    }
    private String getProgressBar(int progress, int total) {
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (i < progress) {
                bar.append("§c█");
            } else {
                bar.append("§7█");
            }
        }
        return bar.toString();
    }
    public static ItemStack getLeaderboard() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Kevos"));
            skullMeta.setDisplayName("§4§lLeaderBoard §f- §cServers");
            skull.setItemMeta(skullMeta);
            skullMeta.setLore(Arrays.asList("§7Every Players Levels"));
        }

        return skull;
    }
    public static ItemStack getUnlock() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Addelburgh"));
            skullMeta.setDisplayName("§4§lUnlocked §f §cItems");
            skull.setItemMeta(skullMeta);
            skullMeta.setLore(Arrays.asList("§7Every items you have unlocked !"));
        }

        return skull;
    }

}
