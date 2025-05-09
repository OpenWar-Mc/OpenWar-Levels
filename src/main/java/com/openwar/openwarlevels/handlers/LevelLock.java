package com.openwar.openwarlevels.handlers;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarlevels.utils.Tuple;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LevelLock implements Listener {
    private LevelSaveAndLoadBDD data;
    private FactionManager fm;
    private JavaPlugin main;
    public static Map<Material, Integer> RP = new HashMap<>();
    public static Map<Material, Integer> LOCK = new HashMap<>();
    public static Map<Tuple<Material, Integer, Integer>, Integer> RECOMPENSE = new HashMap<>();
    public LevelLock(JavaPlugin main, LevelSaveAndLoadBDD data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        loadLock();
        loadRaidPoints();
    }
    public static String formatString(String input) {
        if (input.contains(":")) {
            input = input.split(":")[1];
        }
        return capitalizeWords(input.replace("_", " "));
    }

    private static String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formatted.toString().trim();
    }
    public static ArrayList<String> getWhatUnlocked(int lvl) {
        ArrayList<String> unlockedItems = new ArrayList<>();
        for (Map.Entry<Material, Integer> entry : LOCK.entrySet()) {
            if (entry.getValue() == lvl) {
                String oneItem = formatString(entry.getKey().toString());
                unlockedItems.add(oneItem);
            }
        }
        return unlockedItems;
    }


    //@EventHandler
    //public void onInteract(PlayerInteractEvent event) {
    //    Player player = event.getPlayer();
    //    int level = player.getLevel();
    //    Block block = event.getClickedBlock();
    //    Material type = block.getType();
    //    if (LOCK.containsKey(type)) {
    //        int requiredLevel = LOCK.get(type);
    //        if (requiredLevel > level) {
    //            event.setCancelled(true);
    //            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8» §cYou need to be level: §4" + requiredLevel + " §c!"));
    //        }
    //    }
    //}

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.isOp()) {return;}
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();
        Block block = event.getBlock();
        Material type = block.getType();
        if (LOCK.containsKey(type)) {
            int requiredLevel = LOCK.get(type);
            if (requiredLevel > level) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8You need to be level: §c" + requiredLevel + " "));
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.isOp()) { return; }
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();
        Material type = event.getRecipe().getResult().getType();
        if (LOCK.containsKey(type)) {
            int requiredLevel = LOCK.get(type);
            if (requiredLevel > level) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8You need to be level: §c" + requiredLevel + " "));
            }
        }
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(fm.isAdmin(player.getUniqueId())) {
            return;}
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();

        if (event.getItem() != null) {
            Material itemType = event.getItem().getType();
            if (LOCK.containsKey(itemType)) {
                int requiredLevel = LOCK.get(itemType);
                if (requiredLevel > level) {
                    event.setCancelled(true);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8You need to be level: §c" + requiredLevel + " §8to use this item"));
                } else {
                    if (RP.containsKey(itemType)) {
                        int requiredRP = RP.get(itemType);
                        Faction fac = fm.getFactionByPlayer(player.getUniqueId());
                        if (fac != null && requiredRP <= fac.getRaidPoint()) {
                            int initialAmount = event.getItem().getAmount();
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                int currentAmount = 0;
                                if (player.getInventory().getItemInMainHand().getType() == itemType) {
                                    currentAmount = player.getInventory().getItemInMainHand().getAmount();
                                }
                                if (currentAmount < initialAmount) {
                                    fac.removeFactionPoint(requiredRP);
                                    List<Player> members = fac.getOnlineMembers();
                                    for (Player ms : members) {
                                        ms.sendMessage("§c- §4§l"+requiredRP+" §7Raid Points §k§8§l!!");
                                    }
                                }
                            }, 20L);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private void loadRaidPoints() {
        //ITEM, RP
        RP.put(Material.matchMaterial("hbm:grenade_generic"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_strong"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_mk2"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_nuke"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_breach"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_burst"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_mirv"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_electric"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_generic"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_bouncy"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_sticky"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_impact"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_incendiary"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_smart"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_lemon"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_shrapnel"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_cluster"), 4);
        RP.put(Material.matchMaterial("hbm:grenade_if_he"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_fire"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_concussion"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_plasma"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_if_toxic"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_gas"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_poison"), 3);
        RP.put(Material.matchMaterial("hbm:grenade_tau"), 2);
        RP.put(Material.matchMaterial("hbm:grenade_if_brimstone"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_cloud"), 4);
        RP.put(Material.matchMaterial("hbm:grenade_gascan"), 1);
        RP.put(Material.matchMaterial("hbm:grenade_pink_cloud"), 4);
    }




    public static ItemStack getReward(int playerLevel) {
        if (playerLevel % 5 == 0) {
            ItemStack key = new ItemStack(Material.matchMaterial("hbm:key"));
            ItemMeta meta = key.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§7Open §aLevel Crate");
            meta.setLore(lore);
            meta.setDisplayName("§8» §aLevel §8«");
            key.setItemMeta(meta);
            return key;
        }
        return null;
    }

    public static void loadLock() {
        LOCK.put(Material.matchMaterial("hbm:det_charge"), 10);
        LOCK.put(Material.matchMaterial("hbm:grenade_generic"), 12);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_generic"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_strong"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_flare"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_electric"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_bouncy"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_sticky"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_impact"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_frag"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_smart"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_lemon"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_incendiary"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_shrapnel"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_cluster"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_he"), 18);
        LOCK.put(Material.matchMaterial("hbm:grenade_fire"), 18);
        LOCK.put(Material.matchMaterial("hbm:grenade_mk2"), 18);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_concussion"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_gas"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_plasma"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_toxic"), 20);
        LOCK.put(Material.matchMaterial("hbm:grenade_mirv"), 21);
        LOCK.put(Material.matchMaterial("hbm:grenade_poison"), 21);
        LOCK.put(Material.matchMaterial("hbm:missile_anti_ballistic"), 22);
        LOCK.put(Material.matchMaterial("hbm:grenade_tau"), 23);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_brimstone"), 24);
        LOCK.put(Material.matchMaterial("hbm:grenade_cloud"), 24);
        LOCK.put(Material.matchMaterial("hbm:det_charge"), 30);
        LOCK.put(Material.matchMaterial("mwc:ammo_press"), 33);
        LOCK.put(Material.matchMaterial("hbm:sat_radar"), 46);
        LOCK.put(Material.matchMaterial("hbm:emp_bomb"), 45);
        LOCK.put(Material.matchMaterial("hbm:railgun_plasma"), 53);
        LOCK.put(Material.matchMaterial("hbm:missile_emp"), 47);
        LOCK.put(Material.matchMaterial("hbm:field_disturber"), 50);
        LOCK.put(Material.matchMaterial("hbm:missile_emp_strong"), 50);
        LOCK.put(Material.matchMaterial("hbm:sat_laser"), 56);
        LOCK.put(Material.matchMaterial("hbm:machine_microwave"), 60);
        LOCK.put(Material.matchMaterial("hbm:struct_launcher_core_large"), 30);
        LOCK.put(Material.matchMaterial("hbm:struct_soyuz_core"), 50);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_10_he"), 32);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_10_incendiary"), 34);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_10_buster"), 36);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_15_he"), 40);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_15_incendiary"), 42);
        LOCK.put(Material.matchMaterial("hbm:turret_light"), 28);
        LOCK.put(Material.matchMaterial("hbm:turret_heavy"), 30);
        LOCK.put(Material.matchMaterial("hbm:turret_rocket"), 34);
        LOCK.put(Material.matchMaterial("hbm:turret_flamer"), 32);
        LOCK.put(Material.matchMaterial("hbm:turret_tau"), 40);
        LOCK.put(Material.matchMaterial("hbm:turret_maxwell"), 52);
        LOCK.put(Material.matchMaterial("hbm:turret_tauon"), 50);
        LOCK.put(Material.matchMaterial("hbm:turret_jeremy"), 48);
        LOCK.put(Material.matchMaterial("hbm:turret_friendly"), 42);
        LOCK.put(Material.matchMaterial("hbm:turret_richard"), 53);
        LOCK.put(Material.matchMaterial("hbm:turret_chekhov"), 56);
        LOCK.put(Material.matchMaterial("hbm:turret_fritz"), 46);
        LOCK.put(Material.matchMaterial("mwc:weapon_workbench"), 40);
        LOCK.put(Material.matchMaterial("hbm:missile_soyuz0"), 40);
    }
}
