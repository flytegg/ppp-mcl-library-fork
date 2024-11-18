package org.mclicense.library;

import org.bukkit.plugin.java.JavaPlugin;

public class MCLicense {
    public static boolean validateKey(JavaPlugin plugin, String pluginId) {
        return LicenseValidator.validate(plugin, pluginId);
    }
}