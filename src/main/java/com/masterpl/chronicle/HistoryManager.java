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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryManager {

    private final Chronicles plugin;
    private final NamespacedKey uuidKey;
    private final SimpleDateFormat dateFormat;
    private final DatabaseManager db;
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

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
        if (item == null) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

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
        if (item == null) return;

        String creator = "Unknown";
        if (key.equals("history.forged") && args.length > 0) {
            creator = args[0];
        }

        UUID uuid = getOrCreateItemUUID(item, creator);
        if (uuid == null) return;

        Map<String, Long> itemCooldowns = cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        long lastTime = itemCooldowns.getOrDefault(key, 0L);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime < 10000) {
            return;
        }
        itemCooldowns.put(key, currentTime);

        db.addHistoryEntry(uuid, key, args);
    }

    public CompletableFuture<List<String>> getHistory(ItemStack item) {
        UUID uuid = getItemUUID(item);
        if (uuid == null) return CompletableFuture.completedFuture(new ArrayList<>());

        return db.getHistory(uuid).thenApply(entries -> {
            List<String> translatedHistory = new ArrayList<>();
            
            for (HistoryEntry entry : entries) {
                String dateStr = dateFormat.format(new Date(entry.getTimestamp()));
                String msg = plugin.getLanguageManager().getMessage(entry.getKey(), entry.getArgs());
                translatedHistory.add("§8- §7" + dateStr + " §r" + msg);
            }
            return translatedHistory;
        });
    }

    public boolean hasHistory(ItemStack item) {
        return getItemUUID(item) != null;
    }
}
