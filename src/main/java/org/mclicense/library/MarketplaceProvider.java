package org.mclicense.library;

// Supported marketplaces: Polymart
public class MarketplaceProvider {
    private static String pmPlaceholder = "%%__POLYMART__%%";
    private static String pmLicense = "%%__LICENSE__%%";

    protected static String getHardcodedLicense() {
        if (pmPlaceholder.equals("1") && !pmLicense.startsWith("%%__")) {
            return "pm_" + pmLicense;
        }
        return null;
    }
}