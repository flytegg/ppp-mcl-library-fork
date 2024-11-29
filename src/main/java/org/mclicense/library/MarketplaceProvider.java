package org.mclicense.library;

// Supported marketplaces: Polymart
public class MarketplaceProvider {
    private static String pmPlaceholder = "%%__POLYMART__%%";
    private static String pmLicense = "%%__LICENSE__%%";

    protected static String getHardcodedLicense() {
        if (!pmPlaceholder.equals("%%__POLYMART__%%") && !pmLicense.equals("%%__LICENSE__%%")) {
            return "pm_" + pmLicense;
        }
        return null;
    }
}