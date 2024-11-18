package org.mclicense.library.util;

public class SignedResponse {
    final String data;
    final String signature;

    SignedResponse(String data, String signature) {
        this.data = data;
        this.signature = signature;
    }
}