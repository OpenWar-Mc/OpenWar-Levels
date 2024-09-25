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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class PlayerHandler implements Listener {

    private final PlayerDataManager data;
    private final FactionManager fm;
    private final JavaPlugin main;

    private double experience;
    private int expfac;
    private long lastExpTime;
    private final long EXP_TIMEOUT = 4000;
    private Map<Material, Double> BLOCK = new HashMap<>();
    private Map<Material, Double> CROPS = new HashMap<>();

    public PlayerHandler(JavaPlugin main, PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        System.out.println("Faction Manager loaded !");
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
                    showExp(player, exp);
                    lastExpTime = System.currentTimeMillis();
                    PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
                    playerLevel.setExperience((double) (playerLevel.getExperience() + exp), player);
                    data.savePlayerData(player.getUniqueId(), playerLevel);
                    if (fm == null) {
                        System.out.println("FM IS NULL");
                    }
                    Faction fac = fm.getFactionByPlayer(player.getUniqueId());
                    if (fac != null) {
                        checkFactionXp(player,fac);
                        System.out.println("Faction : "+fac.getName());
                    }
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
            expfac+= (int)exp;
            showExp(player, exp);
            lastExpTime = System.currentTimeMillis();
            PlayerLevel playerLevel = data.loadPlayerData(player.getUniqueId());
            playerLevel.setExperience((double) (playerLevel.getExperience() + exp), player);
            data.savePlayerData(player.getUniqueId(), playerLevel);
            if (fm == null) {
                System.out.println("FM IS NULL");
            }
            Faction fac = fm.getFactionByPlayer(player.getUniqueId());
            if (fac != null) {
                checkFactionXp(player,fac);
                System.out.println("Faction : "+fac.getName());
            }
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
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    public void showExp(Player player, double exp) {
        experience += exp;
        String formattedExp = String.format("%.1f", experience);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f+ §6" + formattedExp + " §8XP"));
    }
    private void checkFactionXp(Player player, Faction fac) {
        int facXP = fac.getExp();
        int facLVL = fac.getLevel();
        float requiredXP = facLVL*23.6F;
        System.out.println("Required Exp to gain faction exp: "+requiredXP);
        if (expfac >= requiredXP) {
            expfac = 0;
            int xp = calcFac(player.getLevel());
            fac.addExp(xp);
        }
    }
    private int calcFac(int playerLVL) {
        int exp = 0;
        exp = playerLVL*10+playerLVL/2+8;
        System.out.println("Experience gagner par la faction: "+exp);
        return exp;
    }
}