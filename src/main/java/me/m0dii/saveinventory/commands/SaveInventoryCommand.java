package me.m0dii.saveinventory.commands;

import me.m0dii.saveinventory.SaveInventory;
import me.m0dii.saveinventory.utils.InventoryHandler;
import me.m0dii.saveinventory.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SaveInventoryCommand implements CommandExecutor, TabCompleter {
    private final InventoryHandler handler = InventoryHandler.getInstance();
    private final FileConfiguration cfg;
    private final SaveInventory plugin;
    public SaveInventoryCommand(SaveInventory plugin) {

        this.cfg = plugin.getCfg();
        this.plugin = plugin;
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args) {
        if(isArgument(0, args, "reload")) {
            if(!sender.hasPermission("saveinventory.command.reload")) {
                sender.sendMessage(Utils.format(cfg.getString("messages.no-permission")));

                return true;
            }

            plugin.getConfigManager().reloadConfig();

            sender.sendMessage(Utils.format(cfg.getString("messages.reloaded")));

            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot perform this command.");
            return true;
        }

        if (!player.hasPermission("saveinventory.command.save")) {
            sender.sendMessage(Utils.format(cfg.getString("messages.no-permission")));

            return true;
        }

        handler.saveInventory(player);

        player.sendMessage(Utils.format(cfg.getString("messages.saved")));

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
                                      @Nonnull String alias, @Nonnull String[] args) {
        List<String> completes = new ArrayList<>();

        if(args.length == 1) {
            completes.add("reload");
        }

        return completes;
    }


    private boolean isArgument(int index, String[] args, String argument) {
        if(args.length == 0 || args.length < index) {
            return false;
        }

        return args[index].equalsIgnoreCase(argument);
    }
}