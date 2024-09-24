package com.openwar.openwarlevels.handlers;

import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class LevelLock {
    private PlayerDataManager data;
    private JavaPlugin main;
    private Map<Material, Integer> LOCK = new HashMap<>();

    public LevelLock(JavaPlugin main, PlayerDataManager data) {
        this.data = data;
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int level = player.getLevel();


    }

    public void loadLock() {
        LOCK.put(Material.matchMaterial("Det Miner$hbm:det_charge"), 10);
        LOCK.put(Material.matchMaterial("Grenade$hbm grenade generic"), 12);
        LOCK.put(Material.matchMaterial("IF - Grenade$hbm grenade if generic"), 15);
        LOCK.put(Material.matchMaterial("Enhanced Grenade$hbm grenade strong"), 15);
        LOCK.put(Material.matchMaterial("Signal Flare$hbm grenade flare"), 15);
        LOCK.put(Material.matchMaterial("Lightning Bomb$hbm grenade electric"), 15);
        LOCK.put(Material.matchMaterial("Impulse Grenade$hbm grenade pulse"), 15);
        LOCK.put(Material.matchMaterial("IF - Bouncy Grenade$hbm grenade if bouncy"), 16);
        LOCK.put(Material.matchMaterial("IF - Sticky Grenade$hbm grenade if sticky"), 16);
        LOCK.put(Material.matchMaterial("IF - Impact Grenade$hbm grenade if impact"), 16);
        LOCK.put(Material.matchMaterial("Frag Grenade$hbm grenade frag"), 16);
        LOCK.put(Material.matchMaterial("Smart Grenade$hbm grenade smart"), 16);
        LOCK.put(Material.matchMaterial("Combustible Lemon$hbm grenade lemon"), 16);
        LOCK.put(Material.matchMaterial("IF - Incendiary Grenade$hbm grenade if incendiary"), 17);
        LOCK.put(Material.matchMaterial("Shrapnel Grenade$hbm grenade shrapnel"), 17);
        LOCK.put(Material.matchMaterial("Cluster Bomb$hbm grenade cluster"), 17);
        LOCK.put(Material.matchMaterial("Breaching Grenade$hbm grenade breach"), 17);
        LOCK.put(Material.matchMaterial("IF - HE Grenade$hbm grenade if he"), 18);
        LOCK.put(Material.matchMaterial("Flame Frag Grenade$hbm grenade fire"), 18);
        LOCK.put(Material.matchMaterial("MK2 Grenade$hbm grenade mk2"), 18);
        LOCK.put(Material.matchMaterial("Diesel Generator$hbm machine diesel"), 19);
        LOCK.put(Material.matchMaterial("Radial performance engine$hbm machine selenium"), 19);
        LOCK.put(Material.matchMaterial("IF - Concussion Grenade$hbm grenade if concussion"), 19);
        LOCK.put(Material.matchMaterial("Gas Grenade$hbm grenade gas"), 19);
        LOCK.put(Material.matchMaterial("Plasma Grenade$hbm grenade plasma"), 19);
        LOCK.put(Material.matchMaterial("IF - toxic Grenade$hbm grenade if toxic"), 20);
        LOCK.put(Material.matchMaterial("MIRV Grenade$hbm grenade mirv"), 21);
        LOCK.put(Material.matchMaterial("Poison Grenade$hbm grenade poison"), 21);
        LOCK.put(Material.matchMaterial("Anti Ballistic$hbm missile anti ballistic"), 22);
        LOCK.put(Material.matchMaterial("Missile T1$hbm missile generic"), 22);
        LOCK.put(Material.matchMaterial("Missile Incendiary T1$hbm missile incendiary"), 22);
        LOCK.put(Material.matchMaterial("Missile Cluster T1$hbm missile cluster"), 22);
        LOCK.put(Material.matchMaterial("Missile Buster T1$hbm missile buster"), 22);
        LOCK.put(Material.matchMaterial("Digger Grenade$hbm grenade burst"), 22);
        LOCK.put(Material.matchMaterial("Tau Grenade$hbm grenade tau"), 23);
        LOCK.put(Material.matchMaterial("Tossable Brimstone Mine$hbm grenade if brimstone"), 24);
        LOCK.put(Material.matchMaterial("Jar Of Cloud$hbm grenade cloud"), 24);
        LOCK.put(Material.matchMaterial("Nuka Grenade$hbm grenade nuclear"), 25);
        LOCK.put(Material.matchMaterial("Multi Purpose Bomb$hbm bomb multi"), 30);
        LOCK.put(Material.matchMaterial("Explosive Charge$hbm det charge"), 30);
        LOCK.put(Material.matchMaterial("Block of Semtex$hbm bomb multi"), 30);
        LOCK.put(Material.matchMaterial("Missile T2$hbm missile strong"), 30);
        LOCK.put(Material.matchMaterial("Missile Incendiary T2$hbm missile incendiary strong"), 30);
        LOCK.put(Material.matchMaterial("Missile Cluster T2$hbm missile cluster strong"), 30);
        LOCK.put(Material.matchMaterial("Missile Buster T2$hbm missile buster strong"), 30);
        LOCK.put(Material.matchMaterial("Solinium Grenade$hbm grenade solinium"), 30);
        LOCK.put(Material.matchMaterial("Ammo Press$mwc ammo press"), 33);
        LOCK.put(Material.matchMaterial("N² Charge$hbm det n2"), 34);
        LOCK.put(Material.matchMaterial("Survey Satellite$hbm sat radar"), 56);
        LOCK.put(Material.matchMaterial("Missile T3$hbm missile burst"), 38);
        LOCK.put(Material.matchMaterial("Missile Incendiary T3$hbm missile inferno"), 38);
        LOCK.put(Material.matchMaterial("Missile Cluster T3$hbm missile rain"), 38);
        LOCK.put(Material.matchMaterial("Missile Buster T3$hbm missile drill"), 38);
        LOCK.put(Material.matchMaterial("Endothermic bomb$hbm therm endo"), 40);
        LOCK.put(Material.matchMaterial("Exothermic bomb$hbm therm exo"), 40);
        LOCK.put(Material.matchMaterial("Missile Micro Nuclear$hbm missile micro"), 40);
        LOCK.put(Material.matchMaterial("Nuclear charge$hbm det nuke"), 41);
        LOCK.put(Material.matchMaterial("Weapon Workbench$mwc weapon workbench"), 43);
        LOCK.put(Material.matchMaterial("Little Boyyyyyy$hbm nuke boy"), 44);
        LOCK.put(Material.matchMaterial("Absorber Pink$hbm absorber pink"), 45);
        LOCK.put(Material.matchMaterial("Custom Missile Nuclear Small$hbm mp warhead 10 nuclear strong"), 45);
        LOCK.put(Material.matchMaterial("N² Mine$hbm nuke n2"), 45);
        LOCK.put(Material.matchMaterial("EMP Device$hbm emp bomb"), 45);
        LOCK.put(Material.matchMaterial("Missile Endo$hbm missile endo"), 45);
        LOCK.put(Material.matchMaterial("Missile Exo$hbm missile exo"), 45);
        LOCK.put(Material.matchMaterial("Railgun$hbm railgun plasma"), 45);
        LOCK.put(Material.matchMaterial("Missile EMP$hbm missile emp"), 46);
        LOCK.put(Material.matchMaterial("Bismuth Anvil$hbm anvil bismuth"), 48);
        LOCK.put(Material.matchMaterial("Gadget$hbm nuke gadget"), 48);
        LOCK.put(Material.matchMaterial("High Energy Field Jammer$hbm field disturber"), 50);
        LOCK.put(Material.matchMaterial("Missile EMP strong$hbm missile emp strong"), 50);
        LOCK.put(Material.matchMaterial("Schrabidium Grenade$hbm grenade schrabidium"), 50);
        LOCK.put(Material.matchMaterial("Fat Man$hbm nuke man"), 52);
        LOCK.put(Material.matchMaterial("The Blue Rinse$hbm nuke solinium"), 55);
        LOCK.put(Material.matchMaterial("Missile N²$hbm missile n2"), 55);
        LOCK.put(Material.matchMaterial("Death Ray$hbm sat laser"), 56);
        LOCK.put(Material.matchMaterial("Crashed DUD$hbm crashed bomb"), 60);
        LOCK.put(Material.matchMaterial("Missile Doomsday$hbm missile doomsday"), 60);
        LOCK.put(Material.matchMaterial("Ivy Mike$hbm nuke mike"), 61);
        LOCK.put(Material.matchMaterial("F.L.E.I.J.A.$hbm nuke fleija"), 64);
        LOCK.put(Material.matchMaterial("Missile Schrab$hbm missile schrabidium"), 65);
        LOCK.put(Material.matchMaterial("Tossable Antischrabidium Cell$hbm grenade aschrab"), 65);
        LOCK.put(Material.matchMaterial("Missile Nuclear$hbm missile nuclear"), 70);
        LOCK.put(Material.matchMaterial("Tsar Bomba$hbm nuke tsar"), 75);
        LOCK.put(Material.matchMaterial("Xenium Resonator Satellite$hbm sat resonator"), 75);
        LOCK.put(Material.matchMaterial("Missile Thermonuclear$hbm missile nuclear cluster"), 75);
        LOCK.put(Material.matchMaterial("Microwave$hbm machine microwave"), 80);
        LOCK.put(Material.matchMaterial("Custom Nuke$hbm nuke custom"), 80);
    }
}
