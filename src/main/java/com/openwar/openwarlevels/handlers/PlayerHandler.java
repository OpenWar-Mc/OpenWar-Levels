package com.openwar.openwarlevels.handlers;

import com.openwar.openwarlevels.level.PlayerDataManager;
import org.bukkit.event.Listener;

public class PlayerHandler implements Listener {

    private final PlayerDataManager data;


    public PlayerHandler(PlayerDataManager data) {
        this.data = data;
    }


    
}
