package org.mclicense.library;

public class MarketplaceDetector {
    enum Marketplace {
        BUILTBYBIT("bbb_"),
        POLYMART("pm_"),
        SPIGOTMC_OR_NONE("");

        private String prefix;

        Marketplace(String prefix) {
            this.prefix = prefix;
        }
    }

    private static String bbbPlaceholder = "%%__BUILTBYBIT__%%";
    private static String pmPlaceholder = "%%__POLYMART__%%";

    protected static Marketplace getMarketplace() {
        if (bbbPlaceholder.equals("true")) return Marketplace.BUILTBYBIT;
        if (pmPlaceholder.equals("1")) return Marketplace.POLYMART;
        return Marketplace.SPIGOTMC_OR_NONE;
    }
}