package com.openwar.openwarlevels.GUI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.OfflinePlayer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    private static ItemBuilder instance;
    private static final Map<String, String> cachedTextures = new HashMap<>();
    private FileConfiguration textureConfig;
    private File textureFile;

    private ItemBuilder() {}

    public static ItemBuilder getInstance() {
        if (instance == null) {
            instance = new ItemBuilder();
        }
        return instance;
    }

    public void loadTextures(File pluginDataFolder) {
        textureFile = new File(pluginDataFolder, "textures.yml");
        if (!textureFile.exists()) {
            try {
                pluginDataFolder.mkdirs();
                textureFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        textureConfig = YamlConfiguration.loadConfiguration(textureFile);
        if (!textureConfig.isConfigurationSection("player_heads")) {
            textureConfig.createSection("player_heads");
        }

        textureConfig.getConfigurationSection("player_heads").getKeys(false).forEach(playerName -> {
            String texture = textureConfig.getString("player_heads." + playerName);
            cachedTextures.put(playerName, texture);
        });
    }

    public ItemStack setPlayerHead(String playerName) {
        String encodedTexture = cachedTextures.get(playerName);
        if (encodedTexture == null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            encodedTexture = fetchTextureFromExternalSource(player);
            cachedTextures.put(playerName, encodedTexture);
            textureConfig.set("player_heads." + playerName, encodedTexture);
            saveTextures();
        }
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), playerName);
        profile.getProperties().put("textures", new Property("textures", encodedTexture));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    private void saveTextures() {
        try {
            textureConfig.save(textureFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fetchTextureFromExternalSource(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString().replace("-", "");
        String texture = null;

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(reader);
                JsonObject response = jsonElement.getAsJsonObject();
                reader.close();
                JsonObject properties = response.getAsJsonArray("properties").get(0).getAsJsonObject();
                texture = properties.get("value").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture != null ? texture : "Base64EncodedTextureExample";
    }
}

