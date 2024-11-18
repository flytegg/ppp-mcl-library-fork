package org.mclicense.library;

class Constants {
    // API
    static final String API_URL = "http://localhost:7071/validate/%s/%s";
    static final int TIMEOUT_MS = 10000;

    // File
    static final String LICENSE_FILE_NAME = "mclicense.txt";

    // Validation
    static final int TIMESTAMP_VALIDITY_SECONDS = 30;

    // Logger
    static final String LOGGER_NAME = "MC License";

    // Crypto
    static final String PUBLIC_KEY =
            "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArhw7oQaOrgCzUxDi5D+N\n" +
            "TH9te0JYB1EvW7CEI40+n2drmHJ4+g0CXXYjJc5LyuePskSUPnnHf3UkRvi1GTUd\n" +
            "6Bqi2Jpeu+qbBfm3hg6rcyLUWo8d5MrBQDbVcIvKmQNegTaJGxFRpEFR9XOeHI1g\n" +
            "4dfF+hOfy+1rbEF4p4fgiz0irtKv8l3uSPOKVoEjTL9xnZx4MU5rIsn6W3jee04q\n" +
            "ESPJpCg8nmmZSuJ+9EzzoLnLnUc2/sBuqJ/jexpNfMrXIR11+L8DFqei7J2M7aKi\n" +
            "0KZvQNIqzqTPBCR9VLZPBjFu6cYT/E/WUjjFROuRhi+7Xsa6tKLqoiI7hXAlKufk\n" +
            "zwIDAQAB\n" +
            "-----END PUBLIC KEY-----";
}