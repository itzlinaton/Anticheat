package org.example.azheng.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.azheng.anticheat.Anticheat;
import org.example.azheng.anticheat.config.ConfigObject;
import org.example.azheng.anticheat.utils.ReflectionUtils;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        ConfigObject messages = Anticheat.instance.configLoader.getConfig("messages.yml");

        Player target;

        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(
                        messages.getMessage(
                                "messages.usage-message",
                                "%usage%", "ping <player>"
                        )
                );
                return true;
            }

            target = (Player) sender;

        } else {

            if (args.length > 1) {
                sender.sendMessage(
                        messages.getMessage(
                                "messages.usage-message",
                                "%usage%", "ping <player>"
                        )
                );
                return true;
            }

            target = Bukkit.getPlayerExact(args[0]);
        }

        if (target == null) {
            sender.sendMessage(
                    messages.getMessage(
                            "messages.player-not-found-message"
                    )
            );
            return true;
        }

        try {
            sender.sendMessage(
                    messages.getMessage(
                            "messages.ping-message",
                            "%player%", target.getName(),
                            "%ping%", ReflectionUtils.getPing(target)
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