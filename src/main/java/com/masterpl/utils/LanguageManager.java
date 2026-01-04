package com.masterpl.utils;

import com.masterpl.Chronicles;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class LanguageManager {

    private final Chronicles plugin;
    private FileConfiguration messages;
    private final Random random;

    public LanguageManager(Chronicles plugin) {
        this.plugin = plugin;
        this.random = new Random();
        loadLanguage();
    }

    public void loadLanguage() {
        String lang = plugin.getConfig().getString("language", "pt");
        File langFile = new File(plugin.getDataFolder(), "lang/messages_" + lang + ".yml");

        if (!langFile.exists()) {
            plugin.saveResource("lang/messages_" + lang + ".yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(langFile);
        
        // Carregar defaults do jar se faltar algo
        InputStream defStream = plugin.getResource("lang/messages_" + lang + ".yml");
        if (defStream != null) {
            messages.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defStream, StandardCharsets.UTF_8)));
        }
    }

    public String getMessage(String key, String... args) {
        String msg;
        
        if (messages.isList(key)) {
            List<String> list = messages.getStringList(key);
            if (list.isEmpty()) {
                msg = "Missing key: " + key;
            } else {
                msg = list.get(random.nextInt(list.size()));
            }
        } else {
            msg = messages.getString(key);
        }

        if (msg == null) return "Missing key: " + key;

        for (int i = 0; i < args.length; i++) {
            msg = msg.replace("%arg" + i + "%", args[i]);
        }
        
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public String getRawMessage(String key) {
        return messages.getString(key, key);
    }
}
