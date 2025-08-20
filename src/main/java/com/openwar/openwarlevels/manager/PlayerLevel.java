package com.openwar.openwarlevels.manager;


import com.openwar.openwarlevels.handlers.LevelLock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerLevel {

    private static final int[] expRequis = {
            0, 318, 930, 2201, 4170, 6772, 10089, 14180, 18981, 24596, 31023, 38210,
            46296, 55211, 64927, 75619, 87145, 99531, 112915, 127155, 142320, 158481,
            175526, 193559, 212579, 232507, 253484, 275438, 298320, 322313, 347268,
            373189, 400244, 428260, 457300, 487456, 518591, 550806, 584118, 618424,
            653868, 690386, 727914, 766636, 806408, 847214, 889252, 932323, 976482,
            1021848, 1068261, 1115817, 1164553, 1214348, 1265342, 1317487, 1370702,
            1425173, 1480765, 1537439
    };

    private int level;
    private double experience;
    private int kills;
    private int deaths;
    private double gain;
    private double loss;
    private long playTime;

    public PlayerLevel(int level, double experience, int kills, int deaths, double gain, double loss, long playTime) {
        this.level = level;
        this.experience = experience;
        this.kills = kills;
        this.deaths = deaths;
        this.gain = gain;
        this.loss = loss;
        this.playTime = playTime;
    }

    public int getLevel() { return level; }
    public double getExperience() { return experience; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public double getGain() { return gain; }
    public double getLoss() { return loss; }
    public long getPlayTime() { return playTime; }
    public void setLevel(int level) { this.level = level; }
    public void setExperience(double exp) { this.experience = exp; }
    public void setKills(int kills) { this.kills = kills; }
    public void setDeaths(int deaths) { this.deaths = deaths; }
    public void setGain(double gain) { this.gain = gain; }
    public void setLoss(double loss) { this.loss = loss; }
    public void setPlayTime(long playTime) { this.playTime = playTime; }
    public void addExperience(double exp, Player p) {
        this.experience += exp;
        checkLevelUp(p);
    }
    public void addKills(int kills) { this.kills += kills; }
    public void addDeaths(int deaths) { this.deaths += deaths; }
    public void addGain(double gain) { this.gain += gain; }
    public void addLoss(double loss) { this.loss += loss; }
    public void addPlayTime(long playTime) { this.playTime += playTime; }

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
            if (!items.isEmpty()) {
                for (String item : items) {
                    player.sendMessage("§8- §4Unlocked §c" + item);
                }
            }
            int y = level % 3;
            if (y == 0) {
                ItemStack key = LevelLock.getReward(level);
                player.sendMessage("§8- §3Rewards §8» §7You earn §aLevel - Crate §7key !");
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(key);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), key);
                    player.sendMessage("§8- §cRewards dropped on ground, inventory full !");
                }
            }
        }
    }
}
