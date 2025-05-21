package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.util.PasswordHasher;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;
    
    public UserService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            isConnectionError = false;
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
            isConnectionError = true;
        }
    }
    
    /**
     * Get a user by ID
     */
    public User getUserById(int userId) {
        if (isConnectionError) return null;
        
        String query = "SELECT * FROM user WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Get a user by username
     */
    public User getUserByUsername(String username) {
        if (isConnectionError) return null;
        
        String query = "SELECT * FROM user WHERE user_name = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by username: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Update user profile (username and phone number)
     */
    public boolean updateUserProfile(int userId, String username, String phoneNumber) {
        if (isConnectionError) return false;
        
        String query = "UPDATE user SET user_name = ?, phone_number = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, phoneNumber);
            pstmt.setInt(3, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user profile: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Check if the provided password matches the user's current password
     */
    public boolean verifyPassword(int userId, String password) {
        if (isConnectionError) return false;
        
        String query = "SELECT user_password FROM user WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("user_password");
                    // Here we need to verify using the same hashing method used during registration
                    return PasswordHasher.verifyPassword(password, storedPassword);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error verifying password: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Update user password
     */
    public boolean updatePassword(int userId, String newPassword) {
        if (isConnectionError) return false;
        
        String query = "UPDATE user SET user_password = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            // Hash the password before storing it
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating password: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Update user profile image
     */
    public boolean updateProfileImage(int userId, String imagePath) {
        if (isConnectionError) return false;
        
        String query = "UPDATE user SET user_image = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating profile image: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Map result set to User object
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setUserPassword(rs.getString("user_password"));
        user.setUserRole(rs.getString("user_role"));
        user.setUserEmail(rs.getString("user_email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setUserImage(rs.getString("user_image"));
        return user;
    }
    
    public void close() {
        if (dbConn != null) {
            try {
                DbConfig.closeConnection(dbConn);
            } catch (Exception e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}