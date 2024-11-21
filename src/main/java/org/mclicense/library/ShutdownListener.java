package org.mclicense.library;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ShutdownListener implements Listener {
    private final JavaPlugin activePlugin;

    public ShutdownListener(JavaPlugin plugin) {
        this.activePlugin = plugin;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == activePlugin && HeartbeatManager.heartbeatTask != null) {
            HeartbeatManager.heartbeatTask.cancel();
            HeartbeatManager.heartbeatTask = null;

            try {
                HeartbeatManager.sendHeartbeat(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}