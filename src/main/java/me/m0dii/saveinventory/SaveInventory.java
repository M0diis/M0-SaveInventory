package me.m0dii.saveinventory;

import me.m0dii.pllib.UpdateChecker;
import me.m0dii.pllib.data.ConfigManager;
import me.m0dii.saveinventory.commands.RestoreInventoryCommand;
import me.m0dii.saveinventory.commands.SaveInventoryCommand;
import me.m0dii.saveinventory.listeners.PlayerListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveInventory extends JavaPlugin {
    private static SaveInventory instance;
    private PluginManager pm;
    private ConfigManager configManager;

    public static SaveInventory getInstance() {
        return instance;
    }

    public FileConfiguration getCfg() {
        return this.configManager.getConfig();
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);

        this.pm = this.getServer().getPluginManager();

        getLogger().info("SaveInventory has been enabled.");
        registerCommands();
        checkForUpdates();
    }

    private void checkForUpdates() {
        new UpdateChecker(this, 106730).getVersion(ver ->
        {
            String curr = this.getDescription().getVersion();

            if (!curr.equalsIgnoreCase(ver.replace("v", ""))) {
                getLogger().info("You are running an outdated version of SaveInventory.");
                getLogger().info("Latest version: " + ver + ", you are using: " + curr);
                getLogger().info("You can download the latest version on Spigot:");
                getLogger().info("https://www.spigotmc.org/resources/106730/");
            }
        });
    }

    public void onDisable() {
        this.getLogger().info("SaveInventory has been disabled.");
    }

    private void registerCommands() {
        PluginCommand cmdRestore = getCommand("restoreinventory");

        if (cmdRestore != null) {
            cmdRestore.setExecutor(new RestoreInventoryCommand(this));
        }

        PluginCommand cmdSave = getCommand("saveinventory");

        if (cmdSave != null) {
            cmdSave.setExecutor(new SaveInventoryCommand(this));
        }

        pm.registerEvents(new PlayerListener(this), this);
    }
}