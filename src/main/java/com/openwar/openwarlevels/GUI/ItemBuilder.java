package com.openwar.openwarlevels.GUI;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack is;
    private static Map<String, String> cachedTextures = new HashMap<>();

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public static void loadTextures(File pluginDataFolder) {
        File file = new File(pluginDataFolder, "textures.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String playerName : config.getConfigurationSection("player_heads").getKeys(false)) {
            String texture = config.getString("player_heads." + playerName);
            cachedTextures.put(playerName, texture);
        }
    }

    public ItemBuilder setPlayerHead(String playerName) {
        String encodedTexture = cachedTextures.get(playerName);
        if (encodedTexture == null) return this;

        SkullMeta headMeta = (SkullMeta) this.is.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "head");
        profile.getProperties().put("textures", new Property("textures", encodedTexture));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.is.setItemMeta(headMeta);
        return this;
    }

    public ItemStack build() {
        return this.is;
    }
}
