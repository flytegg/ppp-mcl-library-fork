package org.mclicense.library.validator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

class LicenseAPIClient {
    private static final String API_URL = "http://localhost:7071/validate/%s/%s";
    private static final int TIMEOUT_MS = 10000;
    private static final Logger LOGGER = Logger.getLogger("MC License");

    static LicenseResponse validateLicense(String pluginId, String key, String serverIp) {
        try {
            URL url = new URL(String.format(API_URL, pluginId, key) + "?serverIp=" + serverIp);
            HttpURLConnection connection = createConnection(url);

            String response = readResponse(connection);
            if (connection.getResponseCode() != 200) {
                LOGGER.info("License validation failed: " + response);
                return null;
            }

            return new LicenseResponse(new JSONObject(response));
        } catch (Exception e) {
            LOGGER.info("Error making API request: " + e.getMessage());
            return null;
        }
    }

    private static HttpURLConnection createConnection(URL url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getResponseCode() >= 400 ?
                        connection.getErrorStream() : connection.getInputStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            return responseBuilder.toString();
        }
    }
}