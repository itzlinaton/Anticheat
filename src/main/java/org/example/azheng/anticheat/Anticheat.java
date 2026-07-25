package org.example.azheng.anticheat;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.azheng.anticheat.checks.*;
import org.example.azheng.anticheat.commands.*;
import org.example.azheng.anticheat.config.*;
import org.example.azheng.anticheat.data.*;
import org.example.azheng.anticheat.listeners.*;
import org.example.azheng.anticheat.utils.*;

public final class Anticheat extends JavaPlugin {

    public static Anticheat instance;

    /* =========================================
       DATA MANAGERS
     ========================================= */

    public DataManager dataManager;
    public TargetTracker targetTracker;
    public ConfigLoader configLoader;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;

        PacketEvents.getAPI().init();

        /* =========================================
           INITIALIZE MANAGERS
         ========================================= */

        dataManager = new DataManager();
        targetTracker = new TargetTracker();

        configLoader = new ConfigLoader(this);
        configLoader.configInit();

        CheckManager checkManager = new CheckManager(this);

        /* =========================================
           INITIALIZE LISTENERS
         ========================================= */

        initializeListeners();

        /* =========================================
           REGISTER CHECKS
         ========================================= */

        checkManager.registerChecks();

        /* =========================================
           SCHEDULERS
         ========================================= */

        // Detects server-process freezes so the Blink check can ignore
        // packet bursts caused by the server catching up.
        Bukkit.getScheduler().runTaskTimer(this, new ServerTick(), 0L, 1L);

        /* =========================================
           COMMANDS
         ========================================= */

        getCommand("ping").setExecutor(new PingCommand());
        getCommand("reload").setExecutor(new ReloadCommand());

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    /* =========================================
       LISTENERS
     ========================================= */

    private void initializeListeners() {
        registerListeners(
                new JoinLeaveListener()
        );

        registerPacketListeners(
                new RotationListener(),
                new MoveListener()
        );
    }

    /* =========================================
       UTILS
     ========================================= */

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerPacketListeners(PacketListener... listeners) {
        for (PacketListener listener : listeners) {
            PacketEvents.getAPI().getEventManager().registerListener(
                    listener, PacketListenerPriority.LOWEST);
        }
    }
}