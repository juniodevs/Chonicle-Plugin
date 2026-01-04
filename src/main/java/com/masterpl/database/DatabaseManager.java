package com.masterpl.database;

import com.masterpl.Chronicles;
import com.masterpl.chronicle.HistoryEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private final Chronicles plugin;
    private Connection connection;
    private final Gson gson;

    public DatabaseManager(Chronicles plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        initializeDatabase();
    }

    private void initializeDatabase() {
        File dbFile = new File(plugin.getDataFolder(), "chronicles.db");
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        try {
            connection = DriverManager.getConnection(url);
            createTables();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to SQLite database", e);
        }
    }

    private void createTables() {
        String itemsTable = "CREATE TABLE IF NOT EXISTS items (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "creator VARCHAR(100)," +
                "created_at LONG" +
                ");";

        String historyTable = "CREATE TABLE IF NOT EXISTS item_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item_uuid VARCHAR(36)," +
                "timestamp LONG," +
                "event_key VARCHAR(100)," +
                "event_args TEXT," +
                "FOREIGN KEY(item_uuid) REFERENCES items(uuid)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(itemsTable);
            stmt.execute(historyTable);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not create tables", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerItem(UUID itemUuid, String creator) {
        String sql = "INSERT OR IGNORE INTO items (uuid, creator, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, itemUuid.toString());
            pstmt.setString(2, creator);
            pstmt.setLong(3, System.currentTimeMillis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHistoryEntry(UUID itemUuid, String key, String[] args) {
        String sql = "INSERT INTO item_history (item_uuid, timestamp, event_key, event_args) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, itemUuid.toString());
            pstmt.setLong(2, System.currentTimeMillis());
            pstmt.setString(3, key);
            pstmt.setString(4, gson.toJson(args));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HistoryEntry> getHistory(UUID itemUuid) {
        List<HistoryEntry> history = new ArrayList<>();
        String sql = "SELECT timestamp, event_key, event_args FROM item_history WHERE item_uuid = ? ORDER BY timestamp ASC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, itemUuid.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                long timestamp = rs.getLong("timestamp");
                String key = rs.getString("event_key");
                String argsJson = rs.getString("event_args");
                String[] args = gson.fromJson(argsJson, String[].class);
                
                history.add(new HistoryEntry(timestamp, key, args));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}
