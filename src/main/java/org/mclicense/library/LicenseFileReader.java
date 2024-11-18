package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;

class LicenseFileReader {
    private static final Logger LOGGER = Logger.getLogger("MC License");

    static String readKey(JavaPlugin plugin) {
        try {
            File licenseFile = new File(plugin.getDataFolder(), "mclicense.txt");
            if (!ensureFileExists(licenseFile)) return null;

            String key = new String(Files.readAllBytes(licenseFile.toPath()), StandardCharsets.UTF_8).trim();
            if (key.isEmpty()) {
                LOGGER.info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return null;
            }
            return key;
        } catch (Exception e) {
            LOGGER.info("Error reading license file: " + e.getMessage());
            return null;
        }
    }

    private static boolean ensureFileExists(File file) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                LOGGER.info("License key is empty! Place your key in the 'mclicense.txt' file in the plugin folder and restart the server.");
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.info("Error creating license file: " + e.getMessage());
            return false;
        }
    }
}