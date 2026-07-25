package org.example.azheng.anticheat.utils;

import org.bukkit.ChatColor;

public class MessageParser {

    /**
     * Converts color codes in a message.
     *
     * @param message Message to parse.
     * @return Colored message.
     */
    public static String parse(String message) {
        if (message == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes(
                '&',
                message
        );
    }
}