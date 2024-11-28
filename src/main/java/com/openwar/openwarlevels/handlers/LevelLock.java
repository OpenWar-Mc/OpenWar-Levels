package com.openwar.openwarlevels.handlers;

import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarlevels.utils.Tuple;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LevelLock implements Listener {
    private PlayerDataManager data;
    private FactionManager fm;
    private JavaPlugin main;
    public static Map<Material, Integer> LOCK = new HashMap<>();
    public static Map<Tuple<Material, Integer, Integer>, Integer> RECOMPENSE = new HashMap<>();
    public LevelLock(JavaPlugin main, PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        loadRecompense();
        loadLock();
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
                String oneItem = formatString(entry.getValue().toString());
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
    public void onThrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.isOp()) {return;}
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();

        if (event.getItem() != null) {
            Material itemType = event.getItem().getType();

            if (LOCK.containsKey(itemType)) {
                int requiredLevel = LOCK.get(itemType);
                if (requiredLevel > level) {
                    event.setCancelled(true);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8You need to be level: §c" + requiredLevel + " §8to use this item"));
                }
            }
        }
    }

    public void loadRecompense() {
        //ITEM, MAX AMOUNT, REQUIRED LVL MIN, CHANCE PERCENT
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:concrete"), 128, 3), 50);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:battery_lithium"), 4, 1),30);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:battery_advanced"), 2, 3),20);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("golden_ingot"), 32, 0),30);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("diamond"), 16, 4),5);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("coal"), 128, 0),50);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:ingot_steel"), 64, 2),40);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:concrete_smooth"), 128, 4),50);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:brick_concrete"), 64, 8),30);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:coil_copper_torus"), 2, 7),20);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:ingot_red_copper"), 64, 6),40);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:circuit_copper"), 10, 6),30);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:circuit_raw"), 15, 3),40);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:battery_generic"), 6, 0),50);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:pipes_steel"), 2, 6),20);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:coil_gold_torus"), 2, 8),30);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:coil_copper"), 4, 3),40);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("iron_ingot"), 64, 0),50);
        RECOMPENSE.put(new Tuple<>(Material.matchMaterial("hbm:ingot_advanced_alloy"), 48, 8),30);
    }

    public static Tuple<String, Material, Integer> getReward(int playerLevel) {
        List<Tuple<Material, Integer, Integer>> rewards = new ArrayList<>(RECOMPENSE.keySet());
        Random random = new Random();

        while (!rewards.isEmpty()) {
            int randomIndex = random.nextInt(rewards.size());
            Tuple<Material, Integer, Integer> candidate = rewards.get(randomIndex);
            Material material = candidate.getFirst();
            int maxAmount = candidate.getSecond();
            int requiredLevel = candidate.getThird();
            int chance = RECOMPENSE.get(candidate);

            if (playerLevel >= requiredLevel) {
                int roll = ThreadLocalRandom.current().nextInt(1, 101);
                if (roll <= chance) {
                    int quantity = calculateQuantity(playerLevel, maxAmount);
                    String name = formatString(material.name());
                    return new Tuple<>(name, material, quantity);
                }
            }
            rewards.remove(randomIndex);
        }

        return null;
    }

    private static int calculateQuantity(int playerLevel, int maxAmount) {
        int baseAmount = ThreadLocalRandom.current().nextInt(2, maxAmount + 1) / 2 * 2;
        double multiplier = 1 + (playerLevel * 0.1);
        return Math.min((int) (baseAmount * multiplier), maxAmount);
        //TODO supposé donner au joueur que des quantité multiple de deux mais ça marche pas parce que c'est avant donc refait stp
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
        LOCK.put(Material.matchMaterial("hbm:grenade_nuclear"), 25);
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
    }
}
