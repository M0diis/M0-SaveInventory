package me.m0dii.saveinventory.utils;

import me.m0dii.saveinventory.SaveInventory;

public class Messenger {
    private static final SaveInventory plugin = SaveInventory.getInstance();

    public static void debug(String msg) {
        if (plugin.getCfg().getBoolean("debug")) {
            plugin.getLogger().info("[DEBUG] " + msg);
        }
    }
}
