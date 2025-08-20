package com.openwar.openwarlevels.handlers;


import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.manager.PlayerDataManager;
import com.openwar.openwarlevels.manager.PlayerLevel;
import com.openwar.openwarlevels.manager.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LevelLock implements Listener {
    private FactionManager fm;
    private PlayerManager pm;
    private JavaPlugin main;
    public static final Map<String, Integer> GRENADE_RP_MAP = new HashMap<>();
    public static final Map<Material, Integer> LOCK = new HashMap<>();
    private final AtomicInteger rpCounter = new AtomicInteger(0);
    private final Map<UUID, Long> cooldowns = new HashMap<>();


    public LevelLock(JavaPlugin main, FactionManager fm, PlayerManager playerManager) {
        this.main = main;
        this.fm = fm;
        this.pm = playerManager;

    }

    static {
        LOCK.put(Material.matchMaterial("hbm:det_charge"), 10);
        LOCK.put(Material.matchMaterial("hbm:grenade_generic"), 12);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_generic"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_strong"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_flare"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_gascan"), 15);
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
        LOCK.put(Material.matchMaterial("hbm:grenade_burst"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_concussion"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_gas"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_plasma"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_toxic"), 20);
        LOCK.put(Material.matchMaterial("hbm:grenade_mirv"), 21);
        LOCK.put(Material.matchMaterial("hbm:grenade_breach"), 21);
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

        LOCK.put(Material.matchMaterial("hbm:turret_maxwell"), 42);
        LOCK.put(Material.matchMaterial("hbm:turret_tauon"), 40);
        LOCK.put(Material.matchMaterial("hbm:turret_jeremy"), 38);
        LOCK.put(Material.matchMaterial("hbm:turret_friendly"), 34);
        LOCK.put(Material.matchMaterial("hbm:turret_richard"), 43);
        LOCK.put(Material.matchMaterial("hbm:turret_chekhov"), 46);
        LOCK.put(Material.matchMaterial("hbm:turret_fritz"), 36);

        LOCK.put(Material.matchMaterial("mwc:weapon_workbench"), 40);
        LOCK.put(Material.matchMaterial("hbm:missile_soyuz0"), 40);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_15_n2"), 50);
    }
    public int getPlayerLevel(UUID playerUUID) {
        return pm.get(playerUUID).getLevel();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.isOp()) {return;}
        int level = getPlayerLevel(player.getUniqueId());
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
        int level = getPlayerLevel(player.getUniqueId());
        Material type = event.getRecipe().getResult().getType();
        if (LOCK.containsKey(type)) {
            int requiredLevel = LOCK.get(type);
            if (requiredLevel > level) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8You need to be level: §c" + requiredLevel + " "));
            }
        }
    }


    public static ItemStack getReward(int playerLevel) {
        ItemStack key = new ItemStack(Material.matchMaterial("hbm:key"));
        ItemMeta meta = key.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7Open §aLevel Crate");
        meta.setLore(lore);
        meta.setDisplayName("§8» §aLevel §8«");
        key.setItemMeta(meta);
        return key;
    }


//Full chat gpt pour le format des noms d'item à partir de leur material, d'ailleurs bukkit va bien te faire enculer à pas mettre les display name tant que le joueur rename pas son putain d'item c'est complètement débile et ntm
    public static String formatItemName(String rawName) {
        String[] parts = rawName.split(":");
        if (parts.length != 2) return rawName;
        String namespace = capitalize(parts[0].toLowerCase());
        String itemPart = parts[1].toLowerCase();
        String[] words = itemPart.split("_");
        StringBuilder formatted = new StringBuilder(namespace);
        for (String word : words) {
            formatted.append(" ").append(capitalize(word));
        }

        return formatted.toString();
    }

    private static String capitalize(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static ArrayList<String> getWhatUnlocked(int lvl) {
        ArrayList<String> unlockedItems = new ArrayList<>();
        for (Map.Entry<Material, Integer> entry : LOCK.entrySet()) {
            if (entry.getValue() == lvl) {
                String oneItem = formatItemName(entry.getKey().toString());
                unlockedItems.add(oneItem);
            }
        }
        return unlockedItems;
    }
}
