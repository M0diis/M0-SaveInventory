package me.m0dii.saveinventory.listeners;

import me.m0dii.saveinventory.SaveInventory;
import me.m0dii.saveinventory.utils.InventoryHandler;
import me.m0dii.saveinventory.utils.PreviewHolder;
import me.m0dii.saveinventory.utils.Utils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {
    private final InventoryHandler handler = InventoryHandler.getInstance();
    private final SaveInventory plugin;

    public PlayerListener(SaveInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        if (!plugin.getCfg().getBoolean("save-on-death")) {
            return;
        }

        Player player = e.getEntity();

        if(!player.hasPermission("saveinventory.save.death")) {
            return;
        }

        handler.saveInventory(player);

        e.getPlayer().sendMessage(Utils.format(plugin.getCfg().getString("messages.inventory-saved-death")));
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        if(!player.hasPermission("saveinventory.save.worldchange")) {
            return;
        }

        World from = e.getFrom();

        if(plugin.getCfg().getStringList("auto-save-on-world-join")
                .stream().anyMatch(player.getWorld().getName()::equalsIgnoreCase)) {
            handler.saveInventory(player);

            player.sendMessage(Utils.format(plugin.getCfg().getString("messages.inventory-saved-world-change")));

            return;
        }

        if(plugin.getCfg().getStringList("auto-restore-on-world-join")
                .stream().anyMatch(player.getWorld().getName()::equalsIgnoreCase)) {
            handler.restoreInventory(player);

            player.sendMessage(Utils.format(plugin.getCfg().getString("messages.inventory-restored-world-change")));

            return;
        }

        if(plugin.getCfg().getStringList("auto-save-on-world-leave")
                .stream().anyMatch(from.getName()::equalsIgnoreCase)) {
            handler.saveInventory(player, from);

            player.sendMessage(Utils.format(plugin.getCfg().getString("messages.inventory-saved-world-change")));

            return;
        }

        if(plugin.getCfg().getStringList("auto-restore-on-world-leave")
                .stream().anyMatch(from.getName()::equalsIgnoreCase)) {
            handler.restoreInventory(player, from);

            player.sendMessage(Utils.format(plugin.getCfg().getString("messages.inventory-restored-world-change")));

            return;
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if(inv != null && inv.getHolder() instanceof PreviewHolder) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        Inventory inv = e.getInventory();

        if(inv.getHolder() instanceof PreviewHolder) {
            e.setCancelled(true);
        }
    }
}
