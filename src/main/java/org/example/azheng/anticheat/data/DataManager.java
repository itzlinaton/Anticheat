package org.example.azheng.anticheat.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class DataManager {

    private final Set<PlayerData> dataSet = new HashSet<>();


    // Creates the data manager and loads online players.
    public DataManager() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    /**
     * Adds player data.
     *
     * @param player Player to add.
     */
    public void add(Player player) {
        dataSet.add(new PlayerData(player));
    }

    /**
     * Gets player data.
     *
     * @param player Player to fetch data for.
     * @return PlayerData or null if not found.
     */
    public PlayerData getPlayerData(Player player) {
        return dataSet.stream()
                .filter(data -> data.player == player)
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes player data.
     *
     * @param player Player to remove.
     */
    public void remove(Player player) {
        dataSet.removeIf(data -> data.player == player);
    }
}