package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.util.PasswordUtil;

public class LoginService {
    private static final Logger logger = Logger.getLogger(LoginService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError = false;

    public LoginService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }

    public User loginUser(User user) {
        if (isConnectionError) {
            logger.severe("Cannot process login due to database connection error");
            return null;
        }

        // Query only by username, we'll verify password after decryption
        String query = "SELECT user_id, user_name, user_password, user_role FROM user WHERE user_name = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            logger.info("Executing login query for user: " + user.getUserName());
            
            try (ResultSet result = stmt.executeQuery()) {
            	if (result.next()) {
            	    // Get encrypted password from database
            	    String encryptedPassword = result.getString("user_password");
            	    String username = result.getString("user_name");
            	    
            	    // Decrypt and validate password
            	    if (validatePassword(user.getUserPassword(), encryptedPassword, username)) {
            	        // Set ALL user details including ID
            	        user.setUserId(result.getInt("user_id"));  // Add this line
            	        user.setUserRole(result.getString("user_role"));
            	        logger.info("Password validated successfully for user: " + username);
            	        return user;
            	    } else {
                        logger.warning("Invalid password for user: " + username);
                        return null;
                    }
                } else {
                    logger.warning("No user found with username: " + user.getUserName());
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error during login: " + e.getMessage());
            return null;
        }
    }

    private boolean validatePassword(String providedPassword, String encryptedPassword, String username) {
        try {
            // Decrypt the stored password
            String decryptedPassword = PasswordUtil.decrypt(encryptedPassword, username);
            
            // Compare the decrypted password with the provided password
            return decryptedPassword != null && decryptedPassword.equals(providedPassword);
        } catch (Exception e) {
            logger.severe("Error during password validation: " + e.getMessage());
            return false;
        }
    }
	/*
	 * // Method for registering new user or updating password public String
	 * encryptPassword(String username, String plainPassword) { try { return
	 * PasswordUtil.encrypt(username, plainPassword); } catch (Exception e) {
	 * logger.severe("Error encrypting password: " + e.getMessage()); return null; }
	 * }
	 */}