package com.openwar.openwarlevels.handlers;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.Main;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class PlayerHandler implements Listener {

    private final PlayerDataManager data;
    private final FactionManager fm;
    private final JavaPlugin main;
    private String logo = "§8» §6Levels 8« §7";

    private double experience;
    private double expboost;
    private int expfac;
    private long lastExpTime;
    private final long EXP_TIMEOUT = 4000;
    private Map<Material, Double> BLOCK = new HashMap<>();
    private Map<Material, Double> CROPS = new HashMap<>();

    public PlayerHandler(JavaPlugin main, PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        loadList();
        startExpTimer();
    }

    @EventHandler
    public void onHarvestCrops(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.CROPS) {
            int datablock = block.getData();
            if (datablock == 7) {
                if (CROPS.containsKey(blockType)) {
                    double exp = CROPS.get(blockType);
                    expManager(player, exp);
                }
            }
        }
    }

    @EventHandler
    public void onMineBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();
        if (block.hasMetadata("no_exp")) {
            return;
        }
        if (BLOCK.containsKey(type)) {
            double exp = BLOCK.get(type);
            expManager(player, exp);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("no_exp", new FixedMetadataValue(main, true));
    }


    public void loadList() {
        BLOCK.put(Material.STONE, 1.4);
        BLOCK.put(Material.EMERALD_ORE, 106.2);
        BLOCK.put(Material.DIAMOND_ORE, 78.5);
        BLOCK.put(Material.GOLD_ORE, 42.8);
        BLOCK.put(Material.IRON_ORE, 23.8);
        BLOCK.put(Material.REDSTONE_ORE, 10.8);
        BLOCK.put(Material.COAL_ORE, 16.2);
        BLOCK.put(Material.matchMaterial("mwc:sulfur_ore"), 22.4);
        BLOCK.put(Material.matchMaterial("mwc:tin_ore"), 20.6);
        BLOCK.put(Material.matchMaterial("mwc:copper_ore"), 20.0);
        BLOCK.put(Material.matchMaterial("mwc:lead_ore"), 22.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_coal_oil"), 10.9);
        BLOCK.put(Material.matchMaterial("hbm:ore_coal_oil burning"), 12.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_aluminium"), 24.7);
        BLOCK.put(Material.matchMaterial("hbm:ore_fluorite"), 21.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_beryllium"), 26.7);
        BLOCK.put(Material.matchMaterial("mwc:graphite_ore"), 25.4);
        BLOCK.put(Material.matchMaterial("hbm:ore_titanium"), 23.1);
        BLOCK.put(Material.matchMaterial("hbm:ore_niter"), 25.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_sulfur"), 22.4);
        BLOCK.put(Material.matchMaterial("hbm:ore_tungsten"), 26.4);
        BLOCK.put(Material.matchMaterial("hbm:ore_copper"), 20.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_thorium"), 26.6);
        BLOCK.put(Material.matchMaterial("hbm:ore_rare"), 32.5);
        BLOCK.put(Material.matchMaterial("hbm:cluster_titanium"), 43.4);
        BLOCK.put(Material.matchMaterial("hbm:cluster_copper"), 42.4);
        BLOCK.put(Material.matchMaterial("hbm:cluster_aluminium"), 42.1);
        BLOCK.put(Material.matchMaterial("hbm:cluster_iron"), 41.6);
        BLOCK.put(Material.matchMaterial("hbm:ore_uranium_scorched"), 50.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_uranium"), 50.1);
        BLOCK.put(Material.matchMaterial("hbm:ore_cobalt"), 45.5);
        BLOCK.put(Material.matchMaterial("hbm:ore_cinnebar"), 31.6);
        BLOCK.put(Material.matchMaterial("hbm:ore_coltan"), 29.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_gold"), 60.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_iron"), 56.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_uranium_scorched"), 58.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_copper"), 50.1);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_asbestos"), 52.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_lithium"), 51.7);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_rare"), 58.1);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_uranium"), 49.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_lignite"), 10.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_unobtainium"), 90.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_daffergon"), 56.6);
        BLOCK.put(Material.matchMaterial("hbm:ore_verticium"), 81.1);
        BLOCK.put(Material.matchMaterial("hbm:ore_reiium"), 83.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_australium"), 87.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_weidaniu"), 80.4);
        BLOCK.put(Material.matchMaterial("hbm:ore_meteor_starmetal"), 123.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_schrabidium"), 266.6);
        BLOCK.put(Material.matchMaterial("hbm:ore_schrabidium"), 231.8);
        BLOCK.put(Material.LOG, 48.6);
        BLOCK.put(Material.CLAY, 34.6);
        BLOCK.put(Material.MELON_BLOCK, 89.6);
        BLOCK.put(Material.PUMPKIN, 89.6);


        CROPS.put(Material.CROPS, 72.4);
        CROPS.put(Material.NETHER_WARTS, 81.9);
    }

    private void startExpTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastExpTime > EXP_TIMEOUT) {
                    experience = 0;
                    expboost = 0;
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    private void expManager(Player player, double exp) {
        expfac+= (int)exp;
        lastExpTime = System.currentTimeMillis();
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId(), fm);
        playerLevel.setExperience((double) (playerLevel.getExperience() + exp), player);
        data.savePlayerData(player.getUniqueId(), playerLevel);
        Faction fac = fm.getFactionByPlayer(player.getUniqueId());
        if (fac != null) {
            checkFactionXp(player,fac, exp);
            double expB= calcExpBoost(player, fac, exp);
            if (expB == 0) {
                return;
            }
            showExp(player, exp, expB);
        }
        showExp(player, exp, 0);
    }

    public void showExp(Player player, double exp, double expb) {
        experience += exp;
        expboost += expb;
        String formattedExp = String.format("%.1f", experience);

        if (expboost > 0) {
            String formattedExpBoost = String.format("%.1f", expboost);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f+ §6" + formattedExp + " §7XP §8§k§l!!§r §7(§3+§b"+formattedExpBoost+"§7)"));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f+ §6" + formattedExp + " §7XP §8§k§l!!"));
        }
    }

    private void checkFactionXp(Player player, Faction fac, double exp) {
        int facLVL = fac.getLevel();
        float requiredXP = facLVL*78.6F;
        if (expfac >= requiredXP) {
            expfac = 0;
            int xp = calcFac(player.getLevel(), exp);
            System.out.println("XP FACTION MAKED "+xp);
            fac.addExp(xp);
        }
    }

    private double calcExpBoost(Player player, Faction faction, double exp) {
        int factionLevel = faction.getLevel();
        if  (factionLevel < 3) {
            return 0;
        }
        if (factionLevel < 6) {
            exp = exp*0.15;
        } else if (factionLevel < 10) {
            exp = exp*0.35;
        } else if (factionLevel < 14) {
            exp = exp*0.55;
        } else if (factionLevel < 18) {
            exp = exp*0.75;
        } else if (factionLevel < 19) {
            exp = exp*0.90;
        } else if (factionLevel == 20) {
            exp = exp;
        }
        PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId(), fm);
        playerLevel.setExperience((double) (playerLevel.getExperience() + exp), player);
        data.savePlayerData(player.getUniqueId(), playerLevel);
        return exp;
    }

    private int calcFac(int playerLVL, double exp) {
        int xp = (int)exp;
        return 2*playerLVL+xp/2;
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        expboost = 0;
        experience = 0;
    }
}