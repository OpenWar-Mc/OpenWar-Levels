package com.openwar.openwarlevels.commands;

import com.openwar.openwarlevels.GUI.LevelGUI;
import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {
    private String logo = "§8» §6Levels 8« §7";
    private String usage = "§fUsage: §c/lvl";
    private PlayerDataManager playerDataManager;
    private LevelGUI gui;

    public LevelCommand(PlayerDataManager playerDataManager, LevelGUI gui) {
        this.playerDataManager = playerDataManager;
        this.gui = gui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(logo + "§cThis command can only be used by players.");
            return true;
        }
        Player player = (((Player) sender).getPlayer());
        gui.openLevelGUI(player);
      return true;
    }
}
