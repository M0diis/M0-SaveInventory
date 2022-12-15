package me.m0dii.saveinventory.utils;

import me.m0dii.pllib.data.AbstractFile;
import me.m0dii.saveinventory.SaveInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHandler extends AbstractFile {
    private static InventoryHandler instance;
    private final SaveInventory plugin;
    private InventoryHandler(SaveInventory plugin, String fileName) {
        super(plugin, fileName);

        this.plugin = plugin;

        instance = this;
    }

    public static InventoryHandler getInstance() {
        if(instance == null) {
            instance = new InventoryHandler(SaveInventory.getInstance(), "inventories.yml");
        }

        return instance;
    }

    public void saveInventory(Player player) {
        saveInventory(player, player.getWorld());
    }

    public void saveInventory(Player player, World world) {
        PlayerInventory inventory = player.getInventory();

        if(fileCfg.getBoolean("clear-on-save") && player.hasPermission("saveinventory.save.clear")) {
            inventory.clear();

            inventory.setArmorContents(null);

            player.updateInventory();
        }

        String base64 = InventoryUtils.playerInventoryToBase64(inventory);

        if(plugin.getCfg().getBoolean("per-world-save")) {
            this.fileCfg.set("inventories." + player.getUniqueId() + "." + world.getName().toLowerCase(), base64);
        } else {
            this.fileCfg.set("inventories." + player.getUniqueId(), base64);
        }

        this.save();
    }


    public boolean restoreInventory(Player player) {
        return restoreInventory(player, player.getWorld());
    }

    public boolean restoreInventory(Player player, World world) {
        ItemStack[] inventory = getContents(player, world);

        player.getInventory().clear();

        player.getInventory().setArmorContents(null);

        player.updateInventory();

        // (inventory contents indexes 0 - 35, armor 36 - 39, offhand - 40)
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, inventory[i]);
        }

        player.getInventory().setHelmet(inventory[39]);
        player.getInventory().setChestplate(inventory[38]);
        player.getInventory().setLeggings(inventory[37]);
        player.getInventory().setBoots(inventory[36]);

        player.getInventory().setItemInOffHand(inventory[40]);

        if(!plugin.getCfg().getBoolean("keep-last-saved-inventory")) {
            if(plugin.getCfg().getBoolean("per-world-save")) {
                this.fileCfg.set("inventories." + player.getUniqueId() + "." + world.getName().toLowerCase(), null);
            }
            else {
                this.fileCfg.set("inventories." + player.getUniqueId(), null);
            }
        }

        this.save();

        return true;
    }

    public void previewInventory(Player player) {
        ItemStack[] inventory = getContents(player, player.getWorld());

        PreviewHolder holder = new PreviewHolder();

        for (int i = 0; i < 36; i++) {
            holder.setItem(i, inventory[i]);
        }

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);

        ItemMeta meta = filler.getItemMeta();

        meta.displayName(Component.text(" "));

        filler.setItemMeta(meta);

        for (int i = 36; i < 45; i++) {
            holder.setItem(i, filler);
        }

        holder.setItem(45, inventory[39]);
        holder.setItem(46, inventory[38]);
        holder.setItem(47, inventory[37]);
        holder.setItem(48, inventory[36]);

        holder.setItem(49, inventory[40]);

        player.openInventory(holder.getInventory());
    }

    private ItemStack[] getContents(Player player, World world) {
        String base64;

        if(plugin.getCfg().getBoolean("per-world-save")) {
            base64 = this.fileCfg.getString("inventories." + player.getUniqueId() + "." + world.getName().toLowerCase());
        } else {
            base64 = this.fileCfg.getString("inventories." + player.getUniqueId());
        }

        if(base64 == null) {
            return new ItemStack[0];
        }

        ItemStack[] inventory = InventoryUtils.itemStackArrayFromBase64(base64);

        if(inventory == null) {
            return new ItemStack[0];
        }

        return inventory;
    }
}
