// LicenseResponse.java
package org.mclicense.library;

import org.json.JSONObject;

class LicenseResponse {
    private final JSONObject data;
    private final String signature;

    LicenseResponse(JSONObject response) {
        this.signature = response.getString("signature");

        // Create data object without signature
        this.data = new JSONObject(response, JSONObject.getNames(response));
        this.data.remove("signature");
    }

    boolean isTimestampValid(int validitySeconds) {
        long currentTimestamp = System.currentTimeMillis();
        long receivedTimestamp = data.getLong("timestamp");
        return (currentTimestamp - receivedTimestamp) <= validitySeconds * 1000;
    }

    boolean matchesRequest(String key, String pluginId) {
        return key.equals(data.getString("key")) &&
                pluginId.equals(data.getString("pluginId"));
    }

    boolean isValid() {
        return "valid".equals(data.getString("status"));
    }

    JSONObject getData() {
        return data;
    }

    String getSignature() {
        return signature;
    }
}