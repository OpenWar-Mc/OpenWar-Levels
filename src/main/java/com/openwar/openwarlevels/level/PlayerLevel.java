package com.openwar.openwarlevels.level;

import org.bukkit.entity.Player;

public class PlayerLevel {
    int[] expRequis = {
            0, 241, 459, 1121, 2111, 3449, 5151, 7231, 9700, 12569, 15848, 19546, 23670, 28227, 33226, 38672, 44572, 50931, 57756, 65051, 72822, 81074, 89811, 99037, 108758, 118978, 129700, 140928, 152667, 164920, 177691, 190983, 204800, 219144, 234020, 249430, 265378, 281866, 298898, 316476, 334604, 353284, 372519, 392311, 412663, 433578, 455058, 477106, 499724, 522915, 546681, 571024, 595946, 621451, 647539, 674214, 701477, 729331, 757777, 786818, 816455, 846692, 877529, 908969, 941013, 973664, 1006923, 1040793, 1075275, 1110370, 1146081, 1182410, 1219358, 1256927, 1295119, 1333935, 1373377, 1413447, 1454146, 1495476, 1537439
    };
    private int level;
    private int experience;

    public PlayerLevel(int level, int experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience, Player player) {
        this.experience = experience;
        checkLevelUp(player);
    }
    private void checkLevelUp(Player player) {
        int currentLevel = this.level;
        int requiredExp;
        while (currentLevel < expRequis.length - 1 && this.experience >= expRequis[currentLevel + 1]) {
            currentLevel++;
        }
        if (currentLevel != this.level) {
            this.level = currentLevel;
            player.sendMessage("§8» §cYou just rank-up level §4" +level+" §8§k§lIII");
        }
    }
}
