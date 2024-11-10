package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MCLicense {
    private static final String API_URL = "https://api.mclicense.org/validate/";
    private static final int TIMEOUT_MS = 10000;

    public static boolean validateKey(JavaPlugin plugin) {
        try {
            File licenseFile = new File(plugin.getDataFolder(), "mclicense.txt");
            if (!licenseFile.exists()) {
                plugin.getDataFolder().mkdirs();
                licenseFile.createNewFile();
                Logger.getLogger("MC License").info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return false;
            }

            String key = new String(Files.readAllBytes(Paths.get(licenseFile.getPath())), StandardCharsets.UTF_8).trim();
            if (key.isEmpty()) {
                Logger.getLogger("MC License").info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return false;
            }

            String serverAddress = InetAddress.getLocalHost().getHostAddress() + ":" + plugin.getServer().getPort();
            return validateKey(key, serverAddress);
        } catch (IOException e) {
            Logger.getLogger("MC License").info("Error reading license file");
            return false;
        }
    }

    public static boolean validateKey(Path filePath, String serverIp) {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                Logger.getLogger("MC License").info("License key is empty! Place your key in the following file and restart the server: " + filePath);
                return false;
            }

            String key = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8).trim();
            if (key.isEmpty()) {
                Logger.getLogger("MC License").info("License key is empty! Place your key in the following file and restart the server: " + filePath);
                return false;
            }

            return validateKey(key, serverIp);
        } catch (IOException e) {
            Logger.getLogger("MC License").info("Error reading license file");
            return false;
        }
    }

    public static boolean validateKey(Path filePath) {
        return validateKey(filePath, null);
    }

    public static boolean validateKey(String key, String serverIp) {
        try {
            String urlString = API_URL + key;
            if (serverIp != null && !serverIp.isEmpty()) {
                urlString += "?serverIp=" + serverIp;
            }

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            String response = "";
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getResponseCode() >= 400 ?
                            connection.getErrorStream() : connection.getInputStream()))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                response = responseBuilder.toString();
            }

            if (!response.isEmpty()) {
                String message = response.substring(
                        response.indexOf("\"message\":\"") + "\"message\":\"".length(),
                        response.indexOf("\"}")
                );
                Logger.getLogger("MC License").info("Key validation " + (connection.getResponseCode() == 200 ? "succeeded" : "failed") + " (" + message + ")");
            }

            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            Logger.getLogger("MC License").info("Internal error validating license key");
            return false;
        }
    }

    public static boolean validateKey(String key) {
        return validateKey(key, null);
    }
}