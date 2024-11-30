package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.mclicense.library.Constants.HEARTBEAT_URL;
import static org.mclicense.library.Constants.TIMEOUT_MS;

class HeartbeatManager {
    private static String pluginId;
    private static String licenseKey;
    private static String serverIp;
    static BukkitTask heartbeatTask;

    protected static void startHeartbeat(JavaPlugin plugin, String pluginId, String licenseKey, String serverIp) {
        plugin.getServer().getPluginManager().registerEvents(new ShutdownListener(plugin), plugin);

        HeartbeatManager.pluginId = pluginId;
        HeartbeatManager.licenseKey = licenseKey;
        HeartbeatManager.serverIp = serverIp;

        heartbeatTask = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    sendHeartbeat(false);
                } catch (Exception e) {
                    //Constants.LOGGER.warning("Failed to send heartbeat for " + plugin.getName() + ": " + e.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin, Constants.HEARTBEAT_INTERVAL_SECONDS * 20, Constants.HEARTBEAT_INTERVAL_SECONDS * 20);
    }

    protected static void sendHeartbeat(boolean isShutdown) throws Exception {
        URL url = new URL(String.format(HEARTBEAT_URL, pluginId, licenseKey));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        connection.setDoOutput(true);

        // Create JSON payload
        JSONObject payload = new JSONObject();
        payload.put("serverIp", serverIp);
        if (isShutdown) {
            payload.put("shutdown", true);
        }

        // Send the heartbeat
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() != 200) {
            // Ignore
        }
    }
}