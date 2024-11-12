package com.openwar.openwarlevels.GUI;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
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
        if (!file.exists()) {
            try {
                pluginDataFolder.mkdirs();
                file.createNewFile();
                FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(file);
                defaultConfig.set("player_heads.examplePlayer", "Base64EncodedTextureExample");
                defaultConfig.save(file);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String playerName : config.getConfigurationSection("player_heads").getKeys(false)) {
            String texture = config.getString("player_heads." + playerName);
            cachedTextures.put(playerName, texture);
        }
    }

    public ItemStack setPlayerHead(String playerName) {
        String encodedTexture = cachedTextures.get(playerName);
        if (encodedTexture == null) return this.is;

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
        return this.is;
    }
}