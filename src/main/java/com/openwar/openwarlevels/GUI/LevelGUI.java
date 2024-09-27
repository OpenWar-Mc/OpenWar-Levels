package com.openwar.openwarlevels.GUI;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarlevels.handlers.LevelLock;
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
import java.util.List;
import java.util.UUID;

public class LevelGUI {

    private PlayerDataManager playerDataManager;
    private LevelLock lock;


    public LevelGUI(PlayerDataManager playerDataManager, LevelLock lock) {
        this.playerDataManager = playerDataManager;
        this.lock = lock;
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
        Inventory menu = Bukkit.createInventory(null, 27, "§8§k§l!!§r §c§lLevel §f- §c§lGUI §8§k§l!!");
        addBorders(menu, 3);

        //TODO LEADERBOARD, HEAD OF PLAYER INFO, UNLOCK MENU

        menu.setItem(11, getPlayerHeadInfo(player.getName()));
        menu.setItem(13, getLeaderboard());
        menu.setItem(15, getUnlock());

        player.openInventory(menu);
    }
//=================================================== OPEN LEADERBOARDS TO PLAYER ==========================
    public void openLeaderboards(Player player){
        UUID playerUUID = player.getUniqueId();
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §c§lLeaderBoards §f- §c§lOpenWar §8§k§l!!");
        addBorders(menu, 6);



    }

    //====================================================== OPEN UNLOCK GUI ==================================

    public void openUnlock(Player player) {
        UUID playerUUID = player.getUniqueId();
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §c§lUnlock §f- §c§l"+player.getName()+" §8§k§l!!");
        addBorders(menu, 6);
        lock.loadLock();
        int size = LevelLock.LOCK.size();
        for ()
    }

    private void leaderBoards(Inventory inv) {
        int slot = 10;
        int totalItems = 300;
        for (int i = 0; i < totalItems; i++) {



            if (slot == 16 || slot == 25 || slot == 34)
            {
                slot= slot+2;
            }
            slot++;
        }

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
                    "§7Experience §8: §c"+String.format("%.2f", xp)+"§8/§c"+String.format("%.2f", nextLevelXp),
                    "§7Progression §8: " + getProgressBar(progress, 10) + " §c" + String.format("%.2f", percent) + "%"
            ));
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }


    private ItemStack getIconItem(Material material, String name, String lore1, String lore2, String lore3) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore1, lore2, lore3));
        }
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getHeadItem(String headName, String name, String lore1, String lore2, String lore3) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(headName));
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore1, lore2,lore2));
            head.setItemMeta(meta);
        }
        return head;
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
            skullMeta.setLore(Arrays.asList("§7Every players levels !"));
            skull.setItemMeta(skullMeta);
        }

        return skull;
    }
    public static ItemStack getUnlock() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Addelburgh"));
            skullMeta.setDisplayName("§4§lUnlocked §f- §cItems");
            skullMeta.setLore(Arrays.asList("§7Every items you have unlocked !"));
            skull.setItemMeta(skullMeta);
        }

        return skull;
    }

}
