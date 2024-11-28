package com.openwar.openwarlevels.handlers;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.GUI.ItemBuilder;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerHandler implements Listener {

    private final PlayerDataManager data;
    private final FactionManager fm;
    private final JavaPlugin main;
    private String logo = "§8» §6Levels 8« §7";

    private final long EXP_TIMEOUT = 4000;
    private Map<Material, Double> BLOCK = new HashMap<>();
    private Map<Material, Double> CROPS = new HashMap<>();
    private Map<String, Double> MOBS = new HashMap<>();
    private final Map<LivingEntity, Player> lastHit = new HashMap<>();
    private final Map<UUID, Double> experience = new ConcurrentHashMap<>();
    private final Map<UUID, Double> expboost = new ConcurrentHashMap<>();
    private final Map<UUID, Double> expfac = new HashMap<>();
    private final Map<UUID, Long> lastExpTime = new ConcurrentHashMap<>();


    public PlayerHandler(JavaPlugin main, PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        loadList();
        startExpTimer();
        startCleanupTask();
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
                    addXp(player, exp);
                }
            }
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity mob = (LivingEntity) entity;
            if (mob.getType() != EntityType.PLAYER) {
                if (event.getDamager() instanceof Player) {
                    Player player = (Player) event.getDamager();
                    lastHit.put(mob, player);
                }
            }
        }
    }


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity deadMob = (LivingEntity) entity;
            if (lastHit.containsKey(deadMob)) {
                Player killer = lastHit.get(deadMob);
                lastHit.remove(deadMob);
                String mobName = deadMob.getType().name();
                if (MOBS.containsKey(mobName)) {
                    double exp = MOBS.get(mobName);
                    addXp(killer, exp);
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
            addXp(player, exp);
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("no_exp", new FixedMetadataValue(main, true));
    }


    public void loadList() {
        BLOCK.put(Material.STONE, 0.1);
        BLOCK.put(Material.COAL_ORE, 4.45);
        BLOCK.put(Material.IRON_ORE, 8.4);
        BLOCK.put(Material.REDSTONE_ORE, 23.625);
        BLOCK.put(Material.LAPIS_ORE, 216.0);
        BLOCK.put(Material.GOLD_ORE, 84.0);
        BLOCK.put(Material.DIAMOND_ORE, 151.2);
        BLOCK.put(Material.EMERALD_ORE, 302.4);
        BLOCK.put(Material.matchMaterial("mwc:sulfur_ore"), 12.9);
        BLOCK.put(Material.matchMaterial("mwc:tin_ore"), 20.16);
        BLOCK.put(Material.matchMaterial("mwc:copper_ore"), 16.8);
        BLOCK.put(Material.matchMaterial("mwc:lead_ore"), 18.3);
        BLOCK.put(Material.matchMaterial("mwc:graphite_ore"), 22.4);

        BLOCK.put(Material.matchMaterial("hbm:ore_coal_oil"), 50.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_iron"), 60.48);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_gold"), 151.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_uranium"), 72.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_uranium_scorched"), 72.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_copper"), 42.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_asbestos"), 252.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_lithium"), 252.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_gas"), 60.48);
        BLOCK.put(Material.matchMaterial("hbm:ore_uranium"), 43.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_uranium_scorched"), 43.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_thorium"), 43.2);
        BLOCK.put(Material.matchMaterial("hbm:ore_titanium"), 31.5);
        BLOCK.put(Material.matchMaterial("hbm:ore_sulfur"), 37.8);
        BLOCK.put(Material.matchMaterial("hbm:ore_aluminium"), 36.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_copper"), 21.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_fluorite"), 63.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_niter"), 42.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_tungsten"), 18.9);
        BLOCK.put(Material.matchMaterial("hbm:ore_lead"), 28.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_beryllium"), 63.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_rare"), 50.4);
        BLOCK.put(Material.matchMaterial("hbm:ore_lignite"), 31.5);
        BLOCK.put(Material.matchMaterial("hbm:ore_asbestos"), 189.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_cinnebar"), 378.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_cobalt"), 189.0);
        BLOCK.put(Material.matchMaterial("hbm:cluster_iron"), 63.0);
        BLOCK.put(Material.matchMaterial("hbm:cluster_titanium"), 126.0);
        BLOCK.put(Material.matchMaterial("hbm:cluster_aluminium"), 84.0);
        BLOCK.put(Material.matchMaterial("hbm:cluster_copper"), 84.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_uranium"), 40.95);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_uranium_scorched"), 40.95);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_tungsten"), 19.656);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_sulfur"), 6.3);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_fire"), 13.65);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_coal"), 2.559375);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_cobalt"), 163.8);
        BLOCK.put(Material.matchMaterial("hbm:ore_coltan"), 250.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_meteor_starmetal"), 5000.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_schrabidium"), 4320.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_nether_schrabidium"), 4095.0);
        BLOCK.put(Material.matchMaterial("hbm:ore_gneiss_schrabidium"), 7200.0);
        BLOCK.put(Material.LOG, 10.4);
        BLOCK.put(Material.LOG_2, 10.4);

        BLOCK.put(Material.CLAY, 34.6);
        BLOCK.put(Material.MELON_BLOCK, 45.6);
        BLOCK.put(Material.PUMPKIN, 45.6);
        BLOCK.put(Material.SUGAR_CANE_BLOCK, 30.5);

        CROPS.put(Material.CROPS, 42.4);
        CROPS.put(Material.NETHER_WARTS, 30.9);

        MOBS.put("CHICKEN", 34.5);
        MOBS.put("COW", 23.2);
        MOBS.put("DONKEY", 23.9);
        MOBS.put("MULE", 26.9);
        MOBS.put("HORSE", 27.1);
        MOBS.put("SHEEP", 25.3);
        MOBS.put("PIG", 23.2);
        MOBS.put("RABBIT", 10.3);
        MOBS.put("WOLF", 1.2);
        MOBS.put("SQUID", 19.6);

        MOBS.put("ZOMBIE" ,10.5);
        MOBS.put("SKELETON" ,13.2);
        MOBS.put("CREEPER" ,12.3);
        MOBS.put("SPIDER" ,14.9);
        MOBS.put("ENDERMAN" ,67.1);
        MOBS.put("WITCH" ,55.3);
        MOBS.put("GHAST" ,43.2);
        MOBS.put("SILVERFISH" ,10.3);
        MOBS.put("BLAZE" ,18.2);
        MOBS.put("ENDERMITE" ,19.6);
        MOBS.put("HUSK" ,23.9);
        MOBS.put("VINDICATOR" ,27.1);
        MOBS.put("EVOKER" ,45.3);
        MOBS.put("WITHER_SKELETON" ,43.2);
        MOBS.put("STRAY" ,20.3);

    }

    private void startExpTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID k : lastExpTime.keySet()){
                    if (System.currentTimeMillis() - lastExpTime.get(k) > EXP_TIMEOUT) {
                        experience.put(k,0.0);
                        expboost.put(k,0.0);
                    }
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    private void addXp(Player player, double exp) {
        UUID id = player.getUniqueId();
        experience.putIfAbsent(id, 0.0);
        expboost.putIfAbsent(id, 0.0);
        expfac.putIfAbsent(id, 0.0);

        expfac.put(id, exp + expfac.get(id));
        lastExpTime.put(id, System.currentTimeMillis());

        Faction fac = fm.getFactionByPlayer(id);
        double expB = 0D;
        if (fac != null) {
            checkFactionXp(player, fac, exp);
            expB = calcExpBoost(player, fac, exp);
        }
        PlayerLevel playerLevel = data.loadPlayerData(id);
        playerLevel.addExperience(exp, player);
        playerLevel.addExperience(expB, player);
        data.savePlayerData(id, playerLevel);
        experience.put(id, exp + experience.get(id));
        expboost.put(id, expB + expboost.get(id));
        showExp(player, exp, expB);
    }

    public void showExp(Player player, double exp, double expb) {
        UUID id=player.getUniqueId();
        String formattedExp = String.format("%.1f", experience.get(id));
        if (expboost.get(id) > 0.0) {
            String formattedExpBoost = String.format("%.1f", expboost.get(id));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f+ §6" + formattedExp + " §7XP §8§k§l!!§r §7(§3+§b"+formattedExpBoost+"§7)"));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§f+ §6" + formattedExp + " §7XP §8§k§l!!"));
        }
    }

    private void checkFactionXp(Player player, Faction fac, double exp) {
        int facLVL = fac.getLevel();
        float requiredXP = facLVL*278.6F;
        if (expfac.get(player.getUniqueId()) >= requiredXP) {
            expfac.put(player.getUniqueId(), 0.0);
            int xp = calcFac(player.getLevel());
            fac.addExp(xp);
        }
    }

    private double calcExpBoost(Player player, Faction faction, double exp) {
        int factionLevel = faction.getLevel();
        if (factionLevel < 3) return 0;
        if (factionLevel < 6) return exp * 0.15;
        if (factionLevel < 10) return exp * 0.35;
        if (factionLevel < 14) return exp * 0.55;
        if (factionLevel < 18) return exp * 0.75;
        if (factionLevel < 19) return exp * 0.90;
        return exp;
    }

    private int calcFac(int playerLVL) {
        return 2*playerLVL/3;
    }


    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            ItemBuilder.getInstance().setPlayerHead(event.getPlayer().getName());
        }
    }
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        UUID id=event.getPlayer().getUniqueId();
        expboost.put(id, 0.0);
        experience.put(id, 0.0);
    }
    private void startCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                lastHit.clear();
            }
        }.runTaskTimer(main, 0L, 1200L);
    }
}