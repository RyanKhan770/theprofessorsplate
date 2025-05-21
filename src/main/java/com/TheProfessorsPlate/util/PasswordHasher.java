package com.TheProfessorsPlate.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordHasher {
    private static final Logger logger = Logger.getLogger(PasswordHasher.class.getName());
    
    // Salt length in bytes
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash a password using SHA-512 with salt
     */
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = generateSalt();
            
            // Create the hash
            byte[] hash = hashWithSalt(password, salt);
            
            // Encode salt + hash as Base64
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hash);
            
            // Return salt + hash, separated by a dot
            return encodedSalt + "." + encodedHash;
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "Error hashing password", e);
            return null;
        }
    }
    
    /**
     * Verify a password against a stored hash
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split the stored value to get salt and hash
            String[] parts = storedHash.split("\\.");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            
            // Hash the password input with the same salt
            byte[] testHash = hashWithSalt(password, salt);
            
            // Compare the hashes (constant time comparison)
            return MessageDigest.isEqual(hash, testHash);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Generate a random salt
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * Hash a password with the given salt
     */
    private static byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}