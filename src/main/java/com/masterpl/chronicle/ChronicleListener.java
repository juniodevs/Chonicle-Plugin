package com.masterpl.chronicle;

import com.masterpl.Chronicles;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.MerchantInventory;

public class ChronicleListener implements Listener {

    private final Chronicles plugin;
    private final HistoryManager historyManager;
    private final NamespacedKey dropperKey;
    private final NamespacedKey dropTimeKey;
    private final NamespacedKey deathDropKey;

    public ChronicleListener(Chronicles plugin) {
        this.plugin = plugin;
        this.historyManager = new HistoryManager(plugin);
        this.dropperKey = new NamespacedKey(plugin, "dropper_uuid");
        this.dropTimeKey = new NamespacedKey(plugin, "drop_time");
        this.deathDropKey = new NamespacedKey(plugin, "death_drop_owner");
    }

    private boolean isTrackable(Material type) {
        String name = type.name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE") || name.endsWith("_PICKAXE") 
            || name.endsWith("_SHOVEL") || name.endsWith("_HOE") || name.endsWith("_HELMET") 
            || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")
            || type == Material.BOW || type == Material.CROSSBOW || type == Material.TRIDENT 
            || type == Material.SHIELD || type == Material.ELYTRA;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        if (isTrackable(item.getType())) {
            historyManager.addHistory(item, "history.enchanted", event.getEnchanter().getName());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.RESULT) return;
        
        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() == Material.AIR || !isTrackable(result.getType())) return;
        
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            AnvilInventory anvil = (AnvilInventory) event.getInventory();
            ItemStack original = anvil.getItem(0); 
            
            if (original != null) {
                String oldName = original.hasItemMeta() && original.getItemMeta().hasDisplayName() 
                        ? original.getItemMeta().getDisplayName() : "";
                String newName = result.hasItemMeta() && result.getItemMeta().hasDisplayName() 
                        ? result.getItemMeta().getDisplayName() : "";
                
                if (!newName.equals(oldName) && !newName.isEmpty()) {
                    historyManager.addHistory(result, "history.renamed", player.getName(), newName);
                } else {
                    historyManager.addHistory(result, "history.repaired", player.getName());
                }
            }
        }
        else if (event.getInventory().getType() == InventoryType.MERCHANT) {
            historyManager.addHistory(result, "history.traded", player.getName());
        }
    }

    @EventHandler
    public void onShieldBlock(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player defender = (Player) event.getEntity();
        
        if (defender.isBlocking()) {
            boolean isBoss = false;
            switch (event.getDamager().getType()) {
                case ENDER_DRAGON:
                case WITHER:
                case WARDEN:
                case ELDER_GUARDIAN:
                    isBoss = true;
                    break;
            }
            
            if (isBoss || event.getDamage() > 8.0) {
                ItemStack activeItem = null;
                if (defender.getInventory().getItemInMainHand().getType() == Material.SHIELD) {
                    activeItem = defender.getInventory().getItemInMainHand();
                } else if (defender.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                    activeItem = defender.getInventory().getItemInOffHand();
                }

                if (activeItem != null) {
                    String attackerName = event.getDamager().getName();
                    if (event.getDamager() instanceof Player) {
                        attackerName = ((Player) event.getDamager()).getName();
                    }
                    historyManager.addHistory(activeItem, "history.blocked", attackerName);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        
        if (item != null && isTrackable(item.getType())) {
            historyManager.addHistory(item, "history.forged", player.getName());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            if (weapon.getType().isItem() && isTrackable(weapon.getType())) { 
                 switch (event.getEntity().getType()) {
                     case PLAYER:
                     case ENDER_DRAGON:
                     case WITHER:
                     case WARDEN:
                         historyManager.addHistory(weapon, "history.killed", event.getEntity().getName());
                         break;
                     default:
                         break;
                 }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack drop : event.getDrops()) {
            if (drop == null || !drop.hasItemMeta() || !isTrackable(drop.getType())) continue;
            
            ItemMeta meta = drop.getItemMeta();
            meta.getPersistentDataContainer().set(deathDropKey, PersistentDataType.STRING, player.getUniqueId().toString());
            meta.getPersistentDataContainer().set(dropTimeKey, PersistentDataType.LONG, System.currentTimeMillis());
            drop.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item itemEntity = event.getItemDrop();
        if (!isTrackable(itemEntity.getItemStack().getType())) return;

        PersistentDataContainer container = itemEntity.getPersistentDataContainer();
        
        container.set(dropperKey, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());
        container.set(dropTimeKey, PersistentDataType.LONG, System.currentTimeMillis());
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player picker = (Player) event.getEntity();
        Item itemEntity = event.getItem();
        ItemStack itemStack = itemEntity.getItemStack();
        
        if (!isTrackable(itemStack.getType())) return;

        PersistentDataContainer entityContainer = itemEntity.getPersistentDataContainer();
        
        boolean isSelfPickup = false;
        boolean eventLogged = false;

        if (entityContainer.has(dropperKey, PersistentDataType.STRING)) {
            String dropperUuidStr = entityContainer.get(dropperKey, PersistentDataType.STRING);
            long dropTime = entityContainer.getOrDefault(dropTimeKey, PersistentDataType.LONG, 0L);
            
            if (dropperUuidStr != null) {
                if (dropperUuidStr.equals(picker.getUniqueId().toString())) {
                    isSelfPickup = true;
                } else {
                    if (System.currentTimeMillis() - dropTime <= 10000) {
                        Player dropper = plugin.getServer().getPlayer(UUID.fromString(dropperUuidStr));
                        String dropperName = dropper != null ? dropper.getName() : "Unknown";
                        historyManager.addHistory(itemStack, "history.given", dropperName, picker.getName());
                        eventLogged = true;
                    }
                }
            }
        }
        
        // Check for death drop (stored on ItemStack NBT)
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer itemContainer = meta.getPersistentDataContainer();
            
            if (itemContainer.has(deathDropKey, PersistentDataType.STRING)) {
                String victimUuidStr = itemContainer.get(deathDropKey, PersistentDataType.STRING);
                long dropTime = itemContainer.getOrDefault(dropTimeKey, PersistentDataType.LONG, 0L);
                
                if (victimUuidStr != null) {
                    if (victimUuidStr.equals(picker.getUniqueId().toString())) {
                        isSelfPickup = true;
                    } else {
                        if (System.currentTimeMillis() - dropTime <= 10000) {
                            Player victim = plugin.getServer().getPlayer(UUID.fromString(victimUuidStr));
                            String victimName = victim != null ? victim.getName() : plugin.getServer().getOfflinePlayer(UUID.fromString(victimUuidStr)).getName();
                            if (victimName == null) victimName = "Unknown";
                            
                            historyManager.addHistory(itemStack, "history.stolen", victimName, picker.getName());
                            eventLogged = true;
                        }
                    }
                }
                
                // Clean up death tags
                itemContainer.remove(deathDropKey);
                itemContainer.remove(dropTimeKey);
                itemStack.setItemMeta(meta);
                itemEntity.setItemStack(itemStack);
            }

            if (!eventLogged && !isSelfPickup && historyManager.hasHistory(itemStack)) {
                historyManager.addHistory(itemStack, "history.found", picker.getName());
            }
    }
}
