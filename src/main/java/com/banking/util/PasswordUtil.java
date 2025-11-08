package com.banking.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    
    /**
     * Hash a password using SHA-256
     * @param password Plain text password
     * @return Hashed password in hexadecimal format
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Verify a password against a hash
     * @param password Plain text password
     * @param hash Stored password hash
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(hash);
    }

    /**
     * Convert byte array to hexadecimal string
     * @param bytes Byte array
     * @return Hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    /**
     * Generate a random account number
     * @return 10-digit account number
     */
    public static String generateAccountNumber() {
        long number = (long) (Math.random() * 9000000000L) + 1000000000L;
        return String.valueOf(number);
    }
}
