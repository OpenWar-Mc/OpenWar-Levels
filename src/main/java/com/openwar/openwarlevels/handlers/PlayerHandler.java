package com.openwar.openwarlevels.handlers;

import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.level.PlayerDataManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


import java.util.*;

public class PlayerHandler implements Listener {

    private final PlayerDataManager data;
    private final FactionManager fm;

    private double experience;
    private Map<Material, Double> BLOCK = new HashMap<>();
    private Map<Material, Double> CROPS = new HashMap<>();

    public PlayerHandler(PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.fm = fm;
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
        BLOCK.put(Material.LOG, 5.6);
        BLOCK.put(Material.CLAY, 3.6);

        CROPS.put(Material.WHEAT, 72.4);
        CROPS.put(Material.NETHER_WARTS, 1.9);
        CROPS.put(Material.MELON_BLOCK, 89.6);
        CROPS.put(Material.PUMPKIN, 89.6);
        CROPS.put(Material.POTATO, 72.4);
        CROPS.put(Material.CARROT, 72.4);
        CROPS.put(Material.BEETROOT, 72.4);
    }

    @EventHandler
    public void onHarvestCrops(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WHEAT) {
            int data = block.getData();
            if (data == 7) {
                if (CROPS.containsKey(blockType)) {
                    double exp = CROPS.get(blockType);
                    showExp(player, exp);
                    if (experience < 0) {

                    }
                    //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                }
            }
        }
    }


    public void showExp(Player player, double exp) {
        experience+= exp;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "+experience+" "));
    }

}