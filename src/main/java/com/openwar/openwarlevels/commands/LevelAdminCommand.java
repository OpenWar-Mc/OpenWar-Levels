package com.openwar.openwarlevels.commands;

import com.openwar.openwarlevels.Main;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelAdminCommand implements CommandExecutor {
    private String logo = "§8» §6Levels §f- §cAdmin §8« §7";
    private PlayerDataManager playerDataManager;
    private Main main;

    public LevelAdminCommand(PlayerDataManager playerDataManager, Main main) {
        this.playerDataManager = playerDataManager;
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (args[0].toLowerCase()) {
            case "lvl":
                if (args[1] != null) {
                    Player target = Bukkit.getPlayer(args[1]);
                    int level = Integer.parseInt(args[2]);
                    PlayerLevel playerLevel = playerDataManager.loadPlayerData(target.getUniqueId(), null);
                    playerLevel.setLevel(level);
                    sender.sendMessage(logo+"level of §c"+target +" §7set to §c"+playerLevel.getLevel());
                }
                break;
            case "xp":
                if (args[1] != null) {
                    Player target = Bukkit.getPlayer(args[1]);
                    double xp = Integer.parseInt(args[2]);
                    PlayerLevel playerLevel = playerDataManager.loadPlayerData(target.getUniqueId(), null);
                    playerLevel.setExperience(xp, target);
                    sender.sendMessage(logo+"Experience of §c"+target.getName() +" §7set to §c"+playerLevel.getExperience());
                }
                break;

        }


        return true;
    }
}