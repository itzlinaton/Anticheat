package org.example.azheng.anticheat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.example.azheng.anticheat.utils.MessageParser;

public class ConfigObject {

    private final FileConfiguration config;

    /**
     * Creates a config wrapper.
     *
     * @param config The config file.
     */
    public ConfigObject(FileConfiguration config) {
        this.config = config;
    }

    /**
     * Gets a string value.
     *
     * @param path Config path.
     */
    public String getString(String path) {
        return config.getString(path);
    }

    /**
     * Gets an integer value.
     *
     * @param path Config path.
     */
    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Gets a double value.
     *
     * @param path Config path.
     */
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    /**
     * Gets a boolean value.
     *
     * @param path Config path.
     */
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Gets a message and replaces placeholders.
     *
     * @param path Config path.
     * @param placeholders Placeholder values.
     */
    public String getMessage(String path, Object... placeholders) {
        String message = getString(path);

        if (message == null) {
            return "";
        }

        return MessageParser.parse(
                Placeholders.replace(message, placeholders)
        );
    }

    /**
     * Gets the raw config.
     *
     * @return Bukkit config instance.
     */
    public FileConfiguration getRaw() {
        return config;
    }
}