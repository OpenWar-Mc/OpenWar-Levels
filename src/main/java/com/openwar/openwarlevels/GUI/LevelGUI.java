package com.openwar.openwarlevels.GUI;

import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.handlers.MenuHandler;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class LevelGUI{

    private PlayerDataManager playerDataManager;
    private JavaPlugin main;
    private MenuHandler mh;

    public LevelGUI(PlayerDataManager playerDataManager, JavaPlugin main, MenuHandler mh) {
        this.main = main;
        this.playerDataManager = playerDataManager;
        this.mh = mh;
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


        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            ItemStack leaderHead = getPlayerHeadInfo(player.getName());
            Bukkit.getScheduler().runTask(main, () -> {
                menu.setItem(11, leaderHead);
                menu.setItem(13, getHeadItem("Addelburgh", "§4§lLeaderBoard §f- §cServers", "§7Every players levels !", "", ""));
                menu.setItem(15, getHeadItem("Kevos","§4§lUnlocked §f- §cItems", "§7Every items you have unlocked !", "", ""));
                player.updateInventory();
            });
        });
        menu.setItem(11, getIconItem(Material.SKULL_ITEM, "§4§l "+player.getName(), "§7Loading data... ", "", ""));
        menu.setItem(13, getIconItem(Material.SKULL_ITEM, "§4§lLeaderBoard §f- §cServers", "§7Every players levels !", "", ""));
        menu.setItem(15, getIconItem(Material.SKULL_ITEM, "§4§lUnlocked §f- §cItems", "§7Every items you have unlocked !", "", ""));

        player.openInventory(menu);
    }
    //=================================================== OPEN LEADERBOARDS TO PLAYER ==========================
    //public void generateLeaderboardCache() {
    //    System.out.println(" GENERATE LEADERBOARD ");
    //    OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
    //    for (OfflinePlayer target : offlinePlayers) {
    //        UUID uuid = target.getUniqueId();
    //        PlayerLevel playerLevel = playerDataManager.loadPlayerData(uuid, null);
    //        int level = playerLevel.getLevel();
    //        double xp = playerLevel.getExperience();
    //        double nextLevelXp = playerLevel.getExpNextLevel();
//
    //        double percent = (xp / nextLevelXp) * 100;
    //        int progress = (int) ((xp / nextLevelXp) * 10);
    //        int total = 10;
//
    //        ItemStack head = getIconItem(Material.SKULL_ITEM,
    //                "§c" + target.getName(),
    //                "§7Level §8: §c" + level,
    //                "§7Experience §8: §c" + String.format("%.2f", xp) + "§8/§c" + String.format("%.2f", nextLevelXp),
    //                "§7Progression §8: " + getProgressBar(progress, total) + " §c" + String.format("%.2f", percent) + "%"
    //        );
    //        System.out.println(" ADDING  "+uuid);
    //    }
    //}

    public List<UUID> generateLeaderboardsListSorted() {
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        Map<UUID, Integer> playerLevels = new HashMap<>();
        for (OfflinePlayer player : players) {
            UUID playerUUID = player.getUniqueId();
            int level = playerDataManager.loadPlayerData(playerUUID).getLevel();
            playerLevels.put(playerUUID, level);
        }
        List<Map.Entry<UUID, Integer>> sortedEntries = new ArrayList<>(playerLevels.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<UUID> sortedLeaderboards = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : sortedEntries) {
            UUID playerUUID = entry.getKey();
            sortedLeaderboards.add(playerUUID);
        }
        return sortedLeaderboards;
    }
    public void openLeaderBoardPage(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §4§lLeaderBoard §8§k§l!!");
        addBorders(menu, 6);
        int index = 10;
        List<UUID> sortedLeaderboard = generateLeaderboardsListSorted();
        int maxItems = Math.min(37, sortedLeaderboard.size());

        for (int i = 0; i < maxItems; i++) {
            UUID uuid = sortedLeaderboard.get(i);
            OfflinePlayer pl = Bukkit.getOfflinePlayer(uuid);
            int finalIndex = index;
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                ItemStack head = getPlayerHeadInfo(pl.getName());
                Bukkit.getScheduler().runTask(main, () -> {
                    menu.setItem(finalIndex, head);
                    player.updateInventory();
                });
            });
            ItemStack head = getIconItem(Material.SKULL_ITEM, "§4§l " + pl.getName(), "§7Loading data... ", "", "");
            menu.setItem(index, head);


            if (index == 16 || index == 25 || index == 34) {
                index += 2;
            } else if (index == 43) {
                index = 36;
            }
            index++;
        }
        player.openInventory(menu);
    }
    //===================================================== UNLOCK PAGE =============================================================



    public void openUnlockPage(Player player, int page, int totalPages, List<Map.Entry<Material, Integer>> lockList, int playerLevel) {
        Inventory menu = Bukkit.createInventory(null, 54, "§8§k§l!!§r §4§lUnlock §f- §4§l" + player.getName() + " §8§k§l!!§r §8(Page §f" + page + "§8/§f" + totalPages + "§8)");
        addBorders(menu, 6);
        List<Map.Entry<Material, Integer>> sortedLockList = getSortedLockList();
        int startIndex = (page - 1) * 28;
        int endIndex = Math.min(startIndex + 28, sortedLockList.size());

        int index = 10;
        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<Material, Integer> entry = sortedLockList.get(i);
            Material material = entry.getKey();
            int lockLevel = entry.getValue();
            if (material == null) {
                System.err.println("Material at index " + i + " is null.");
                ItemStack itemFinal = getIconItem(Material.BARRIER, "§cConcrete Cracker", "§cRequired level: §438", "§7Item Data Null","");
                menu.setItem(index, itemFinal);
                continue;
            }

            ItemStack itemFinal;
            if (lockLevel > playerLevel) {
                itemFinal = getIconItem(material, "§8» §4Locked §8«", "§f=============", "§cRequired level: §4"+lockLevel, "§f=============");
            } else {
                itemFinal = getIconItem(material, "§8» §2Unlocked §8«", "§f=============", "§aRequired level: §2"+lockLevel, "§f=============");
            }
            menu.setItem(index, itemFinal);
            if (index == 16 || index == 25 || index == 34)
            {
                index= index+2;
            }
            index++;
        }
        addNavigationButtons(menu, page, totalPages);
        mh.setCurrentPage(player.getUniqueId(), page, "unlock");
        player.openInventory(menu);
    }
    public List<Map.Entry<Material, Integer>> getSortedLockList() {
        return LevelLock.LOCK.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    }

    public ItemStack getPlayerHeadInfo(String playerName) {
        ItemStack playerHead = ItemBuilder.getInstance().setPlayerHead(playerName);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        if (meta != null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            PlayerLevel playerLevel = playerDataManager.loadPlayerData(player.getUniqueId());
            int level = playerLevel.getLevel();
            double xp = playerLevel.getExperience();
            double nextLevelXp = playerLevel.getExpNextLevel();

            double percent = (xp / nextLevelXp) * 100;
            int progress = (int) ((xp / nextLevelXp) * 10);
            int total = 10;
            meta.setDisplayName("§4§l" + playerName);
            meta.setLore(Arrays.asList(
                    "§7Level §8: §c" + level,
                    "§7Experience §8: §c"+String.format("%.2f", xp)+"§8/§c"+String.format("%.2f", nextLevelXp),
                    "§7Progression §8: " + getProgressBar(progress, total) + " §c" + String.format("%.2f", percent) + "%"
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
        return playerDataManager.loadPlayerData(playerUUID).getLevel();
    }

    public List<Map.Entry<Material, Integer>> getLockList() {
        return new ArrayList<>(LevelLock.LOCK.entrySet());
    }

    public int getTotalPages(String gui) {
        int i = 0;
        if (gui.equals("unlock")) {
            i = (int) Math.ceil((double) getLockList().size() / 28);
        }
        if (gui.equals("leader")) {
            OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            i = (int) Math.ceil((double) offlinePlayers.length / 28);
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
        ItemStack head = ItemBuilder.getInstance().setPlayerHead(headName);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            lore1 = lore1 != null ? lore1 : "";
            lore2 = lore2 != null ? lore2 : "";
            lore3 = lore3 != null ? lore3 : "";
            meta.setLore(Arrays.asList(lore1, lore2, lore3));
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

}
