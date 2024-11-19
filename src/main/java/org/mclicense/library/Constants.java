package org.mclicense.library;

import java.util.logging.Logger;

class Constants {
    static final String PUBLIC_KEY =
            "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArhw7oQaOrgCzUxDi5D+N\n" +
                    "TH9te0JYB1EvW7CEI40+n2drmHJ4+g0CXXYjJc5LyuePskSUPnnHf3UkRvi1GTUd\n" +
                    "6Bqi2Jpeu+qbBfm3hg6rcyLUWo8d5MrBQDbVcIvKmQNegTaJGxFRpEFR9XOeHI1g\n" +
                    "4dfF+hOfy+1rbEF4p4fgiz0irtKv8l3uSPOKVoEjTL9xnZx4MU5rIsn6W3jee04q\n" +
                    "ESPJpCg8nmmZSuJ+9EzzoLnLnUc2/sBuqJ/jexpNfMrXIR11+L8DFqei7J2M7aKi\n" +
                    "0KZvQNIqzqTPBCR9VLZPBjFu6cYT/E/WUjjFROuRhi+7Xsa6tKLqoiO4VwJSrn5L\n" +
                    "zwIDAQAB\n" +
                    "-----END PUBLIC KEY-----";
    static final String API_URL = "https://api.mclicense.org/validate/%s/%s";
    static final int TIMEOUT_MS = 10000;
    static final int TIMESTAMP_VALIDITY_SECONDS = 30;
    static final Logger LOGGER = Logger.getLogger("MC License");
}