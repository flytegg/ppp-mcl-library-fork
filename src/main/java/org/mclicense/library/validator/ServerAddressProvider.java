package org.mclicense.library.validator;

import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;

class ServerAddressProvider {
    static String getAddress(JavaPlugin plugin) throws Exception {
        return InetAddress.getLocalHost().getHostAddress() + ":" + plugin.getServer().getPort();
    }
}