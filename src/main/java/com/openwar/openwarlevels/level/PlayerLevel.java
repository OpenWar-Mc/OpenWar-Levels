package com.openwar.openwarlevels.level;

import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.handlers.LevelLock;
import com.openwar.openwarlevels.utils.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class PlayerLevel {
    private static final int[] expRequis = {
            0, 318, 930, 2201, 4170, 6772, 10089, 14180, 18981, 24596, 31023, 38210, 46296, 55211, 64927, 75619, 87145, 99531, 112915, 127155, 142320, 158481, 175526, 193559, 212579, 232507, 253484, 275438, 298320, 322313, 347268, 373189, 400244, 428260, 457300, 487456, 518591, 550806, 584118, 618424, 653868, 690386, 727914, 766636, 806408, 847214, 889252, 932323, 976482, 1021848, 1068261, 1115817, 1164553, 1214348, 1265342, 1317487, 1370702, 1425173, 1480765, 1537439
    };
    private int level;
    private double experience;

    public PlayerLevel(int level, double experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }
    public void addExperience(double experience, Player player){
        this.experience+=experience;
        checkLevelUp(player);
    }
    public void setExperience(double experience, Player player) {
        this.experience = experience;
        checkLevelUp(player);
    }

    public double getExpNextLevel() {
        if (level >= expRequis.length - 1) return expRequis[expRequis.length - 1];
        return expRequis[level + 1];
    }

    public double getExpCurrentLevel() {
        return expRequis[level];
    }

    private void checkLevelUp(Player player) {
        int currentLevel = this.level;
        while (currentLevel < expRequis.length - 1 && this.experience >= expRequis[currentLevel + 1]) {
            currentLevel++;
            this.experience-=expRequis[currentLevel];
        }
        if (currentLevel != this.level) {
            this.level = currentLevel;
            player.sendMessage("§8§k§l!!§r §cYou just ranked-up level §4§l" + level + " §8§k§l!!");
            ArrayList<String> items = LevelLock.getWhatUnlocked(level);
            for (String item : items) {
                player.sendMessage("§8- §4Unlocked §c"+item);
            }
            Tuple<String, Material, Integer> recomp = LevelLock.getReward(level);
            assert recomp != null;
            String name = recomp.getFirst();
            Integer amount = recomp.getThird();
            Material material = recomp.getSecond();
            ItemStack item = new ItemStack(material, amount);
            player.sendMessage("§8- §3Rewards §8» §f+"+amount+" §b"+name);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                player.sendMessage("§8- §cRewards dropped on ground, inventory full !");
            }
        }
    }
}