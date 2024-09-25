package com.openwar.openwarlevels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {
    private String logo = "§8» §6Levels 8« §7";
    private String usage = "§fUsage: §c/lvl";


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(logo + "§cThis command can only be used by players.");
            return true;
        }
        Player player = (((Player) sender).getPlayer());



      return true;
    }
}
