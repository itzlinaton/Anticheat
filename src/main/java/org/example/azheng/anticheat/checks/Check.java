package org.example.azheng.anticheat.checks;

import com.github.retrooper.packetevents.event.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.example.azheng.anticheat.Anticheat;
import org.example.azheng.anticheat.config.ConfigObject;
import org.example.azheng.anticheat.utils.WebhookUtil;

public abstract class Check implements Listener, PacketListener {

    private final String name;

    public Check(String name) {
        this.name = name;
    }

    /**
     * Flags a player.
     *
     * @param target      Player who failed the check.
     * @param information Extra flag information (aka verbose).
     */
    public void flag(Player target, String... information) {

        ConfigObject messages = Anticheat.instance.configLoader.getConfig("messages.yml");
        ConfigObject config = Anticheat.instance.configLoader.getConfig("config.yml");

        StringBuilder verbose = new StringBuilder();

        for (String info : information) {
            verbose.append(info);
        }

        String flagMessage = messages.getMessage(
                "messages.flag-message",
                "%prefix%", config.getString("prefix"),
                "%player%", target.getName(),
                "%check%", name,
                "%verbose%", verbose.toString()
        );

        // Send to players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(flagMessage);
        }

        // Send to console if enabled
        if (config.getBoolean("print-console")) {
            Bukkit.getConsoleSender().sendMessage(flagMessage);
        }

        // Send webhook notification if enabled
        WebhookUtil.sendViolation(
                target.getName(),
                name,
                verbose.toString()
        );
    }
}