package org.mclicense.library.validator;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

class SignatureVerifier {
    private static final String PUBLIC_KEY_RAW = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArhw7oQaOrgCzUxDi5D+N\n" +
            "TH9te0JYB1EvW7CEI40+n2drmHJ4+g0CXXYjJc5LyuePskSUPnnHf3UkRvi1GTUd\n" +
            "6Bqi2Jpeu+qbBfm3hg6rcyLUWo8d5MrBQDbVcIvKmQNegTaJGxFRpEFR9XOeHI1g\n" +
            "4dfF+hOfy+1rbEF4p4fgiz0irtKv8l3uSPOKVoEjTL9xnZx4MU5rIsn6W3jee04q\n" +
            "ESPJpCg8nmmZSuJ+9EzzoLnLnUc2/sBuqJ/jexpNfMrXIR11+L8DFqei7J2M7aKi\n" +
            "0KZvQNIqzqTPBCR9VLZPBjFu6cYT/E/WUjjFROuRhi+7Xsa6tKLqoiI7hXAlKufk\n" +
            "zwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    private static final PublicKey PUBLIC_KEY;

    static {
        try {
            String processedKey = PUBLIC_KEY_RAW
                    .replace("-----BEGIN PUBLIC KEY-----\n", "")
                    .replace("\n-----END PUBLIC KEY-----", "")
                    .replaceAll("\n", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(processedKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PUBLIC_KEY = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize public key", e);
        }
    }

    static boolean verify(LicenseResponse response) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(PUBLIC_KEY);
            sig.update(response.getData().toString().getBytes(StandardCharsets.UTF_8));
            return sig.verify(Base64.getDecoder().decode(response.getSignature()));
        } catch (Exception e) {
            return false;
        }
    }
}