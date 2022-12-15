package me.m0dii.saveinventory.commands;

import me.m0dii.saveinventory.SaveInventory;
import me.m0dii.saveinventory.utils.InventoryHandler;
import me.m0dii.saveinventory.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

        if(!player.hasPermission("saveinventory.command.restore")) {
            sender.sendMessage(Utils.format(cfg.getString("messages.no-permission")));

            return true;
        }

        if(player.hasPermission("saveinventory.command.preview") && isArgument(0, args, "preview")) {
            if(args.length == 2) {
                handler.previewInventory(player, player.getWorld(), args[1]);
            } else {
                handler.previewInventory(player);
            }

            return true;
        }

        if(player.hasPermission("saveinventory.command.clear") && isArgument(0, args, "clear")) {
            if(args.length == 2) {
                handler.clearStorage(player, player.getWorld(), args[1], true);

                sender.sendMessage(Utils.format(cfg.getString("messages.storage-cleared")));
            } else {
                handler.clearStorage(player, player.getWorld(), "default", true);

                sender.sendMessage(Utils.format(cfg.getString("messages.storage-cleared")));
            }

            return true;
        }

        if(args.length == 1 && !isArgument(0, args, "preview", "clear")) {
            boolean result = handler.restoreInventory(player, player.getWorld(), args[0]);

            if(result) {
                player.sendMessage(Utils.format(cfg.getString("messages.restored")));
            } else {
                player.sendMessage(Utils.format(cfg.getString("messages.no-inventory-restore")));
            }

            return true;
        } else {
            boolean result = handler.restoreInventory(player);

            if(result) {
                player.sendMessage(Utils.format(cfg.getString("messages.restored")));
            } else {
                player.sendMessage(Utils.format(cfg.getString("messages.no-inventory-restore")));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
                                      @Nonnull String alias, @Nonnull String[] args) {
        List<String> completes = new ArrayList<>();

        if(args.length == 1) {
            Stream.of("preview", "clear", "reload")
                    .filter(s -> StringUtils.startsWithIgnoreCase(s, args[0]))
                    .forEach(completes::add);
        }

        if(sender instanceof Player player) {
            if(args.length == 2 && isArgument(0, args, "preview", "clear")) {
                handler.getSavedInventories(player, player.getWorld())
                        .stream()
                        .filter(s -> StringUtils.startsWithIgnoreCase(s, args[1]))
                        .forEach(completes::add);
            }
        }

        return completes;
    }

    private boolean isArgument(int index, String[] args, String... argument) {
        if(args.length == 0 || args.length < index) {
            return false;
        }

        return Arrays.stream(argument).anyMatch(arg -> args[index].equalsIgnoreCase(arg));
    }


}