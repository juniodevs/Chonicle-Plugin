package com.masterpl.chronicle;

import com.masterpl.Chronicles;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class HistoryCommand implements CommandExecutor {

    private final Chronicles plugin;
    private final HistoryManager historyManager;

    public HistoryCommand(Chronicles plugin) {
        this.plugin = plugin;
        this.historyManager = new HistoryManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("command.only_players"));
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(plugin.getLanguageManager().getMessage("command.no_item"));
            return true;
        }

        List<String> history = historyManager.getHistory(item);

        if (history.isEmpty()) {
            player.sendMessage(plugin.getLanguageManager().getMessage("command.no_history"));
            return true;
        }

        openHistoryBook(player, item, history);
        return true;
    }

    private void openHistoryBook(Player player, ItemStack sourceItem, List<String> history) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle(plugin.getLanguageManager().getMessage("book.title"));
            meta.setAuthor(plugin.getLanguageManager().getMessage("book.author"));

            StringBuilder pageContent = new StringBuilder();
            
            String itemName = sourceItem.hasItemMeta() && sourceItem.getItemMeta().hasDisplayName() 
                    ? sourceItem.getItemMeta().getDisplayName() 
                    : sourceItem.getType().toString();
            
            String intro = plugin.getLanguageManager().getMessage("book.intro").replace("%item_name%", itemName);
            pageContent.append(intro);

            // Título ocupa espaço (estimativa segura de 4 linhas para o cabeçalho)
            int lines = 4; 

            for (String entry : history) {
                String lineText = "§0- " + entry;
                // Estimar quebra de linha (aprox 25 caracteres por linha no livro para evitar cortes)
                int estimatedEntryLines = 1 + (lineText.length() / 25);

                // Se adicionar essa entrada passar de 13 linhas, cria nova página
                if (lines + estimatedEntryLines > 13) {
                    meta.addPage(pageContent.toString());
                    pageContent = new StringBuilder();
                    lines = 0;
                }
                pageContent.append(lineText).append("\n");
                lines += estimatedEntryLines;
            }

            if (pageContent.length() > 0) {
                meta.addPage(pageContent.toString());
            }

            book.setItemMeta(meta);
            player.openBook(book);
        }
    }
}
