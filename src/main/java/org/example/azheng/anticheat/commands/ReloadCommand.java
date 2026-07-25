package org.example.azheng.anticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.example.azheng.anticheat.Anticheat;
import org.example.azheng.anticheat.config.ConfigObject;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        ConfigObject messages = Anticheat.instance.configLoader.getConfig("messages.yml");

        try {
            Anticheat.instance.configLoader.reloadConfigs();

            sender.sendMessage(
                    messages.getMessage(
                            "messages.reload-message"
                    )
            );

        } catch (Exception e) {
            sender.sendMessage(
                    messages.getMessage(
                            "messages.unexpected-error-message",
                            "%error%", e.getMessage()
                    )
            );

            e.printStackTrace();
        }

        return true;
    }
}