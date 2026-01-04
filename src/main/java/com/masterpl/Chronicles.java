package com.masterpl;

import com.masterpl.chronicle.ChronicleListener;
import com.masterpl.chronicle.HistoryCommand;
import com.masterpl.database.DatabaseManager;
import com.masterpl.utils.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Chronicles extends JavaPlugin {

    private LanguageManager languageManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        languageManager = new LanguageManager(this);
        databaseManager = new DatabaseManager(this);
        
        getLogger().info("Chronicles habilitado!");
        
        // Registrar comandos
        getCommand("history").setExecutor(new HistoryCommand(this));
        
        // Registrar eventos
        getServer().getPluginManager().registerEvents(new ChronicleListener(this), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("Chronicles desabilitado!");
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
