package com.camilo.authapp.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public String hash(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            return toHex(encoded);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("No se pudo crear el hash", exception);
        }
    }

    public boolean matches(String plainText, String expectedHash) {
        return hash(plainText).equals(expectedHash);
    }

    private String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte current : bytes) {
            builder.append(String.format("%02x", current));
        }
        return builder.toString();
    }
}