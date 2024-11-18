package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

public class MCLicense2 {
    private static final String PUBLIC_KEY = "";
    private static final String API_URL = "https://api.mclicense.org/validate/%s/%s";
    private static final int TIMEOUT_MS = 10000;
    private static final Logger LOGGER = Logger.getLogger("MC License");

    public static boolean validateKey(JavaPlugin plugin, String pluginId) {
        try {
            File licenseFile = new File(plugin.getDataFolder(), "mclicense.txt");
            if (!licenseFile.exists()) {
                plugin.getDataFolder().mkdirs();
                licenseFile.createNewFile();
                LOGGER.info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return false;
            }

            String key = new String(Files.readAllBytes(Paths.get(licenseFile.getPath())), StandardCharsets.UTF_8).trim();
            if (key.isEmpty()) {
                LOGGER.info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return false;
            }

            String serverIp = InetAddress.getLocalHost().getHostAddress() + ":" + plugin.getServer().getPort();
            URL url = new URL(String.format(API_URL, pluginId, key) + "?serverIp=" + serverIp);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            String response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream()))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                response = responseBuilder.toString();
            }

            if (connection.getResponseCode() != 200) {
                LOGGER.info("License validation failed: " + response);
                return false;
            }

            String data = response.substring(
                    response.indexOf("\"data\":\"") + "\"data\":\"".length(),
                    response.indexOf("\",\"signature\"")
            );

            String signature = response.substring(
                    response.indexOf("\"signature\":\"") + "\"signature\":\"".length(),
                    response.lastIndexOf("\"")
            );

            byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Create signature instance
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));

            // Verify the signature
            boolean isValid = sig.verify(Base64.getDecoder().decode(signature));
            if (!isValid) {
                LOGGER.info("Invalid license signature");
                return false;
            }

            LOGGER.info("License validation succeeded: " + data);
            return true;
        } catch (Exception e) {
            LOGGER.info("Error validating license");
            return false;
        }
    }

}