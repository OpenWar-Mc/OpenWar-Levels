package com.openwar.openwarlevels.handlers;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.level.PlayerDataManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LevelLock implements Listener {
    private PlayerDataManager data;
    private FactionManager fm;
    private JavaPlugin main;
    private Map<Material, Integer> LOCK = new HashMap<>();

    public LevelLock(JavaPlugin main, PlayerDataManager data, FactionManager fm) {
        this.data = data;
        this.main = main;
        this.fm = fm;
        loadLock();
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int level = player.getLevel();
        Block block = event.getClickedBlock();
        Material type = block.getType();
        if (LOCK.containsKey(type)) {
            int requiredLevel = LOCK.get(type);
            if (requiredLevel > level) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8» §cYou need to be level: §4" + requiredLevel + " §c!"));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        int level = player.getLevel();
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

    public void loadLock() {
        LOCK.put(Material.matchMaterial("hbm:det_charge"), 10);
        LOCK.put(Material.matchMaterial("hbm:grenade_generic"), 12);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_generic"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_strong"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_flare"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_electric"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_pulse"), 15);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_bouncy"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_sticky"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_impact"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_frag"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_smart"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_lemon"), 16);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_incendiary"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_shrapnel"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_cluster"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_breach"), 17);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_he"), 18);
        LOCK.put(Material.matchMaterial("hbm:grenade_fire"), 18);
        LOCK.put(Material.matchMaterial("hbm:grenade_mk2"), 18);
        LOCK.put(Material.matchMaterial("hbm:machine_diesel"), 19);
        LOCK.put(Material.matchMaterial("hbm:machine_selenium"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_concussion"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_gas"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_plasma"), 19);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_toxic"), 20);
        LOCK.put(Material.matchMaterial("hbm:grenade_mirv"), 21);
        LOCK.put(Material.matchMaterial("hbm:grenade_poison"), 21);
        LOCK.put(Material.matchMaterial("hbm:missile_anti_ballistic"), 22);
        LOCK.put(Material.matchMaterial("hbm:missile_generic"), 22);
        LOCK.put(Material.matchMaterial("hbm:missile_incendiary"), 22);
        LOCK.put(Material.matchMaterial("hbm:missile_cluster"), 22);
        LOCK.put(Material.matchMaterial("hbm:missile_buster"), 22);
        LOCK.put(Material.matchMaterial("hbm:grenade_burst"), 22);
        LOCK.put(Material.matchMaterial("hbm:grenade_tau"), 23);
        LOCK.put(Material.matchMaterial("hbm:grenade_if_brimstone"), 24);
        LOCK.put(Material.matchMaterial("hbm:grenade_cloud"), 24);
        LOCK.put(Material.matchMaterial("hbm:grenade_nuclear"), 25);
        LOCK.put(Material.matchMaterial("hbm:bomb_multi"), 30);
        LOCK.put(Material.matchMaterial("hbm:det_charge"), 30);
        LOCK.put(Material.matchMaterial("hbm:bomb_multi"), 30);
        LOCK.put(Material.matchMaterial("hbm:missile_strong"), 30);
        LOCK.put(Material.matchMaterial("hbm:missile_incendiary_strong"), 30);
        LOCK.put(Material.matchMaterial("hbm:missile_cluster_strong"), 30);
        LOCK.put(Material.matchMaterial("hbm:missile_buster_strong"), 30);
        LOCK.put(Material.matchMaterial("hbm:grenade_solinium"), 30);
        LOCK.put(Material.matchMaterial("hbm:ammo_press"), 33);
        LOCK.put(Material.matchMaterial("hbm:det_n2"), 34);
        LOCK.put(Material.matchMaterial("hbm:sat_radar"), 56);
        LOCK.put(Material.matchMaterial("hbm:missile_burst"), 38);
        LOCK.put(Material.matchMaterial("hbm:missile_inferno"), 38);
        LOCK.put(Material.matchMaterial("hbm:missile_rain"), 38);
        LOCK.put(Material.matchMaterial("hbm:missile_drill"), 38);
        LOCK.put(Material.matchMaterial("hbm:therm_endo"), 40);
        LOCK.put(Material.matchMaterial("hbm:therm_exo"), 40);
        LOCK.put(Material.matchMaterial("hbm:missile_micro"), 40);
        LOCK.put(Material.matchMaterial("hbm:det_nuke"), 41);
        LOCK.put(Material.matchMaterial("hbm:weapon_workbench"), 43);
        LOCK.put(Material.matchMaterial("hbm:nuke_boy"), 44);
        LOCK.put(Material.matchMaterial("hbm:absorber_pink"), 45);
        LOCK.put(Material.matchMaterial("hbm:mp_warhead_10_nuclear_strong"), 45);
        LOCK.put(Material.matchMaterial("hbm:nuke_n2"), 45);
        LOCK.put(Material.matchMaterial("hbm:emp_bomb"), 45);
        LOCK.put(Material.matchMaterial("hbm:missile_endo"), 45);
        LOCK.put(Material.matchMaterial("hbm:missile_exo"), 45);
        LOCK.put(Material.matchMaterial("hbm:railgun_plasma"), 45);
        LOCK.put(Material.matchMaterial("hbm:missile_emp"), 46);
        LOCK.put(Material.matchMaterial("hbm:anvil_bismuth"), 48);
        LOCK.put(Material.matchMaterial("hbm:nuke_gadget"), 48);
        LOCK.put(Material.matchMaterial("hbm:field_disturber"), 50);
        LOCK.put(Material.matchMaterial("hbm:missile_emp_strong"), 50);
        LOCK.put(Material.matchMaterial("hbm:grenade_schrabidium"), 50);
        LOCK.put(Material.matchMaterial("hbm:nuke_man"), 52);
        LOCK.put(Material.matchMaterial("hbm:nuke_solinium"), 55);
        LOCK.put(Material.matchMaterial("hbm:missile_n2"), 55);
        LOCK.put(Material.matchMaterial("hbm:sat_laser"), 56);
        LOCK.put(Material.matchMaterial("hbm:crashed_bomb"), 60);
        LOCK.put(Material.matchMaterial("hbm:missile_doomsday"), 60);
        LOCK.put(Material.matchMaterial("hbm:nuke_mike"), 61);
        LOCK.put(Material.matchMaterial("hbm:nuke_fleija"), 64);
        LOCK.put(Material.matchMaterial("hbm:missile_schrabidium"), 65);
        LOCK.put(Material.matchMaterial("hbm:grenade_aschrab"), 65);
        LOCK.put(Material.matchMaterial("hbm:missile_nuclear"), 70);
        LOCK.put(Material.matchMaterial("hbm:nuke_tsar"), 75);
        LOCK.put(Material.matchMaterial("hbm:sat_resonator"), 75);
        LOCK.put(Material.matchMaterial("hbm:missile_nuclear_cluster"), 75);
        LOCK.put(Material.matchMaterial("hbm:machine_microwave"), 80);
        LOCK.put(Material.matchMaterial("hbm:nuke_custom"), 80);
    }
}
