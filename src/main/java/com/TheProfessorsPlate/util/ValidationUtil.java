package com.TheProfessorsPlate.util;

import jakarta.servlet.http.Part;

/**
 * @author Ryan Khan
 */

public class ValidationUtil {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isAlphanumericStartingWithLetter(String value) {
        if (isNullOrEmpty(value)) return false;
        return value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }

    public static boolean isValidUserRole(String value) {
        if (isNullOrEmpty(value)) return false;
        return value.equalsIgnoreCase("admin") || value.equalsIgnoreCase("customer");
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhoneNumber(String number) {
        if (isNullOrEmpty(number)) return false;
        // Changed from "98\\d{8}" to "\\d{10}" to allow any 10-digit number
        return number.matches("^\\d{10}$");
    }

    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) return false;
        // Password must be at least 8 characters long
        // Must contain at least 1 uppercase letter
        // Must contain at least 1 number
        // Must contain at least 1 special character
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>])[A-Za-z0-9!@#$%^&*()\\-_=+{};:,<.>]{8,}$");
    }
    
    public static boolean isValidImageExtension(Part imagePart) {
        if (imagePart == null || imagePart.getSize() == 0) {
            return true; // If no image provided, consider it valid
        }
        
        if (isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return false;
        }
        
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }

    public static boolean doPasswordsMatch(String password1, String password2) {
        if (password1 == null || password2 == null) return false;
        return password1.equals(password2);
    }
}