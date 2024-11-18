package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

class LicenseValidator {
    private static final int TIMESTAMP_VALIDITY_SECONDS = 30;
    private static final Logger LOGGER = Logger.getLogger("MC License");

    static boolean validate(JavaPlugin plugin, String pluginId) {
        try {
            String key = LicenseFileReader.readKey(plugin);
            if (key == null) return false;

            String serverIp = ServerAddressProvider.getAddress(plugin);
            LicenseResponse response = LicenseAPIClient.validateLicense(pluginId, key, serverIp);
            if (response == null) return false;

            if (!response.isTimestampValid(TIMESTAMP_VALIDITY_SECONDS)) {
                LOGGER.info("Timestamp validation failed");
                return false;
            }

            if (!response.matchesRequest(key, pluginId)) {
                LOGGER.info("Request matching failed");
                return false;
            }

            if (!SignatureVerifier.verify(response)) {
                LOGGER.info("Signature verification failed");
                return false;
            }

            return response.isValid();
        } catch (Exception e) {
            LOGGER.info("Error validating license: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}