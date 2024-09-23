package com.openwar.openwarlevels.level;

public class PlayerLevel {

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

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLvlExp(int exp, int lvl) {
        this.experience = exp;
        this.level = lvl;
    }
}
