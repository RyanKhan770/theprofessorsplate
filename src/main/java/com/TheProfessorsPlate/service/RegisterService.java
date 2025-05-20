package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.User;

public class RegisterService {
    private Connection dbConn;
    /**
     * Constructor initializes the database connection.
     */
    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public Boolean addUser(User user) {
        if (dbConn == null) {
            System.err.println("Database connection is not available.");
            return null;
        }
        
        String insertQuery = "INSERT INTO user (user_name, user_password, user_role, user_email, " +
                           "phone_number, user_image) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
            // Insert user details
            insertStmt.setString(1, user.getUserName());
            insertStmt.setString(2, user.getUserPassword());
            insertStmt.setString(3, user.getUserRole());
            insertStmt.setString(4, user.getUserEmail());
            insertStmt.setString(5, user.getPhoneNumber());
            insertStmt.setString(6, user.getUserImage());

            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // It's good practice to add a close method
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}