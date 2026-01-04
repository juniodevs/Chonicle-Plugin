package com.masterpl.chronicle;

import com.masterpl.Chronicles;
import com.masterpl.database.DatabaseManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HistoryManager {

    private final Chronicles plugin;
    private final NamespacedKey uuidKey;
    private final SimpleDateFormat dateFormat;
    private final DatabaseManager db;

    public HistoryManager(Chronicles plugin) {
        this.plugin = plugin;
        this.uuidKey = new NamespacedKey(plugin, "item_uuid");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.db = plugin.getDatabaseManager();
    }

    private UUID getItemUUID(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String uuidStr = container.get(uuidKey, PersistentDataType.STRING);
        return uuidStr != null ? UUID.fromString(uuidStr) : null;
    }

    private UUID getOrCreateItemUUID(ItemStack item, String creator) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String uuidStr = container.get(uuidKey, PersistentDataType.STRING);
        
        UUID uuid;
        if (uuidStr == null) {
            uuid = UUID.randomUUID();
            container.set(uuidKey, PersistentDataType.STRING, uuid.toString());
            item.setItemMeta(meta);
            db.registerItem(uuid, creator);
        } else {
            uuid = UUID.fromString(uuidStr);
        }
        return uuid;
    }

    public void addHistory(ItemStack item, String key, String... args) {
        if (item == null || !item.hasItemMeta()) return;

        // Se não tem criador definido ainda, usa "Unknown" ou o primeiro argumento se for forja
        String creator = "Unknown";
        if (key.equals("history.forged") && args.length > 0) {
            creator = args[0];
        }

        UUID uuid = getOrCreateItemUUID(item, creator);
        db.addHistoryEntry(uuid, key, args);
        
        // Update lore with translated message
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        String translated = plugin.getLanguageManager().getMessage(key, args);
        lore.add("§7" + dateFormat.format(new Date()) + " - " + translated);
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public List<String> getHistory(ItemStack item) {
        UUID uuid = getItemUUID(item);
        if (uuid == null) return new ArrayList<>();

        List<HistoryEntry> entries = db.getHistory(uuid);
        List<String> translatedHistory = new ArrayList<>();
        
        for (HistoryEntry entry : entries) {
            String dateStr = dateFormat.format(new Date(entry.getTimestamp()));
            String msg = plugin.getLanguageManager().getMessage(entry.getKey(), entry.getArgs());
            translatedHistory.add(dateStr + " - " + msg);
        }

        return translatedHistory;
    }

    public boolean hasHistory(ItemStack item) {
        return getItemUUID(item) != null;
    }
}
