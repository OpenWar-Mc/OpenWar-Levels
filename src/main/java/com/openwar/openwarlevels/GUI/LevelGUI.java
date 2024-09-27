package com.openwar.openwarlevels.GUI;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

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


        player.openInventory(menu);



    }

    //====================================================== OPEN UNLOCK GUI ==================================


    public void openLeaderBoardPage(Player player, int page, int totalPages) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §4§lLeaderBoard §f- §4§lOpenWar §8§k§l!!§r §8(Page §f" + page + "§8/§f" + totalPages + "§8)");
        addBorders(menu, 6);
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        int startIndex = (page - 1) * 36;
        int endIndex = Math.min(startIndex + 36, offlinePlayers.length);
        int index = 10;
        for (OfflinePlayer target : offlinePlayers) {
            UUID uuid = target.getUniqueId();
            int level = playerDataManager.loadPlayerData(uuid, null).getLevel();
            double xp = playerDataManager.loadPlayerData(uuid, null).getExperience();
            ItemStack head = getHeadItem(target.getName(), "§c"+target.getName(), "§8Level : §c"+ level, "§8Experience : §c"+xp, "");
            for (int i = startIndex; i < endIndex; i++) {
                ItemStack itemFinal;
                menu.setItem(index, head);
                if (index == 16 || index == 25 || index == 34)
                {
                    index= index+2;
                } else if (index == 43) {
                    index = 36;
                }
                index++;
            }
        }
        addNavigationButtons(menu, page, totalPages);
        player.openInventory(menu);
    }

    public void openUnlockPage(Player player, int page, int totalPages, List<Map.Entry<Material, Integer>> lockList, int playerLevel) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §4§lUnlock §f- §4§l" + player.getName() + " §8§k§l!!§r §8(Page §f" + page + "§8/§f" + totalPages + "§8)");
        addBorders(menu, 6);
        List<Map.Entry<Material, Integer>> sortedLockList = getSortedLockList();
        int startIndex = (page - 1) * 36;
        int endIndex = Math.min(startIndex + 36, lockList.size());

        int index = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<Material, Integer> entry = sortedLockList.get(i);
            Material material = entry.getKey();
            int lockLevel = entry.getValue();
            if (material == null) {
                System.err.println("Material at index " + i + " is null.");
                continue;
            }

            ItemStack itemFinal;
            if (lockLevel > playerLevel) {
                itemFinal = getIconItem(material, "§8» §4Locked §8«", "§f=============", "§cRequired level: §4"+lockLevel, "§f=============");
            } else {
                itemFinal = getIconItem(material, "§8» §2Unlocked §8«", "§f=============", "§2Required level: §a"+lockLevel, "§f=============");
            }
            menu.setItem(index, itemFinal);
            if (index == 16 || index == 25 || index == 34)
            {
                index= index+2;
            } else if (index == 43) {
                index = 36;
            }
            index++;
        }

        addNavigationButtons(menu, page, totalPages);
        player.openInventory(menu);
    }
    public List<Map.Entry<Material, Integer>> getSortedLockList() {
        return LevelLock.LOCK.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
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

    private void addNavigationButtons(Inventory menu, int currentPage, int totalPages) {
        if (currentPage > 1) {
            ItemStack prevPage = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName("§8<< §cPrevious Page");
            prevPage.setItemMeta(prevMeta);
            menu.setItem(48, prevPage);
        }

        if (currentPage < totalPages) {
            ItemStack nextPage = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("§aNext Page §8>>");
            nextPage.setItemMeta(nextMeta);
            menu.setItem(50, nextPage);
        }
    }

    public int getPlayerLevel(UUID playerUUID) {
        return playerDataManager.loadPlayerData(playerUUID, null).getLevel();
    }

    public List<Map.Entry<Material, Integer>> getLockList() {
        return new ArrayList<>(LevelLock.LOCK.entrySet());
    }

    public int getTotalPages(String gui) {
        int i = 0;
        if (gui.equals("unlock")) {
            i = (int) Math.ceil((double) getLockList().size() / 36);
        }
        if (gui.equals("leader")) {
            OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            i = (int) Math.ceil((double) offlinePlayers.length / 36);
        }
        return i;
    }


    private ItemStack getIconItem(Material material, String name, String lore1, String lore2, String lore3) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            lore1 = lore1 != null ? lore1 : "";
            lore2 = lore2 != null ? lore2 : "";
            lore3 = lore3 != null ? lore3 : "";
            meta.setLore(Arrays.asList(lore1, lore2, lore3));
            item.setItemMeta(meta);
        }
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
