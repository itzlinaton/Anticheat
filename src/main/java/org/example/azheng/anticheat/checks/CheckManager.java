package org.example.azheng.anticheat.checks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.example.azheng.anticheat.checks.combat.*;
import org.example.azheng.anticheat.checks.movement.*;
import org.example.azheng.anticheat.checks.packet.*;

public class CheckManager {

    private final Plugin plugin;

    public CheckManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerChecks() {

        /* =========================================
           COMBAT CHECKS
         ========================================= */

        //registerPacketChecks(PacketListenerPriority.NORMAL, new KillauraA("Aura (A)"));
        registerPacketChecks(PacketListenerPriority.NORMAL, new KillauraB("Aura (B)"));

        //registerPacketChecks(PacketListenerPriority.NORMAL, new AimA("Aim (A)"));

        // Reach (A) is the old check that doesn't account for movement interpolation or lag.
        // Reach (B) is its replacement

        registerPacketChecks(
                PacketListenerPriority.NORMAL,
                new ReachA("Reach (A)"),
                new ReachB("Reach (B)")
        );


        /* =========================================
           MOVEMENT CHECKS
         ========================================= */

        registerPacketChecks(PacketListenerPriority.NORMAL, new SpeedA("Speed (A)"));
        registerBukkitChecks(new NoFallA("NoFall (A)"));

        //registerBukkitChecks(new NoFallB("NoFall (B)"));


        /* =========================================
           PACKET CHECKS
         ========================================= */

        registerPacketChecks(
                PacketListenerPriority.NORMAL,
                new Timer("Timer"),
                new InvalidPitch("InvalidPitch"),
                new PingSpoof("PingSpoof"),
                new Blink("Blink")
        );
    }


    /* =========================================
       UTILS
     ========================================= */

    private void registerPacketChecks(PacketListenerPriority priority, PacketListener... checks) {
        for (PacketListener check : checks) {
            PacketEvents.getAPI()
                    .getEventManager()
                    .registerListener(
                            check,
                            priority
                    );
        }
    }

    private void registerBukkitChecks(Listener... checks) {
        for (Listener check : checks) {
            Bukkit.getPluginManager()
                    .registerEvents(
                            check,
                            plugin
                    );
        }
    }
}