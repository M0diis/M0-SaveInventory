package me.m0dii.saveinventory.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PreviewHolder implements InventoryHolder {
    private Inventory inventory;

    public PreviewHolder() {
        this.inventory = Bukkit.createInventory(this, 54, Utils.format("&c&lPreview"));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void clear() {
        this.inventory.clear();
    }

    public void setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    public ItemStack getItem(int slot) {
        return this.inventory.getItem(slot);
    }

    public void setContents(ItemStack[] contents) {
        this.inventory.setContents(contents);
    }

    public ItemStack[] getContents() {
        return this.inventory.getContents();
    }
}
