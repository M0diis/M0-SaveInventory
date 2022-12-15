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

public class RestoreInventoryCommand implements CommandExecutor, TabCompleter {
    private final SaveInventory plugin;
    private final FileConfiguration cfg;

    private final InventoryHandler handler = InventoryHandler.getInstance();

    public RestoreInventoryCommand(SaveInventory plugin) {
        this.cfg = plugin.getCfg();
        this.plugin = plugin;
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot perform this command.");
            return true;
        }

        if(!player.hasPermission("saveinventory.command.restore")) {
            sender.sendMessage(Utils.format(cfg.getString("messages.no-permission")));

            return true;
        }

        if(player.hasPermission("saveinventory.command.preview") && isArgument(0, args, "preview")) {
            handler.previewInventory(player);

            return true;
        }

        boolean result = handler.restoreInventory(player);

        if(result) {
            player.sendMessage(Utils.format(cfg.getString("messages.restored")));
        } else {
            player.sendMessage(Utils.format(cfg.getString("messages.no-inventory-restore")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
                                      @Nonnull String alias, @Nonnull String[] args) {
        List<String> completes = new ArrayList<>();

        if(args.length == 1) {
            completes.add("preview");
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