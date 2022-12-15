package me.m0dii.saveinventory.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;

public class Utils {
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String stripColor(Component component) {
        return ChatColor.stripColor(PlainTextComponentSerializer.plainText().serializeOr(component, ""));
    }
}
