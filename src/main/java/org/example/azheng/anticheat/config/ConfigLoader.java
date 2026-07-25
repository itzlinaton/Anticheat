package org.example.azheng.anticheat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.azheng.anticheat.Anticheat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

    private final Anticheat plugin;

    private final Map<String, ConfigObject> configs = new HashMap<>();

    public ConfigLoader(Anticheat plugin) {
        this.plugin = plugin;
    }

    // Loads all plugin config files.
    public void configInit() {
        loadConfig("config.yml");
        loadConfig("messages.yml");
        loadConfig("webhook.yml");

        plugin.getLogger().info("Configuration loading complete. Loaded "
                + configs.size() + " files.");
    }

    // Reloads all currently loaded config files.
    public void reloadConfigs() {
        for (String name : configs.keySet().toArray(new String[0])) {
            loadConfig(name);
        }
    }

    /**
     * Loads a config file.
     *
     * @param name Config file name.
     */
    private void loadConfig(String name) {
        File file = new File(plugin.getDataFolder(), name);

        try {
            if (!file.exists()) {
                plugin.saveResource(name, false);
                plugin.getLogger().info("Created missing config: " + name);
            }

            FileConfiguration configuration =
                    YamlConfiguration.loadConfiguration(file);

            configs.put(
                    name,
                    new ConfigObject(configuration)
            );

            plugin.getLogger().info("Loaded config: " + name);

        } catch (Exception exception) {
            plugin.getLogger().severe("Failed to load config: " + name);
            exception.printStackTrace();
        }
    }

    /**
     * Gets a loaded config.
     *
     * @param name Config file name.
     * @return ConfigObject instance.
     */
    public ConfigObject getConfig(String name) {
        return configs.get(name);
    }
}