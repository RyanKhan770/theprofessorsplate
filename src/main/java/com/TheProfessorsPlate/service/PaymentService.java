package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private Connection dbConn;
    
    public PaymentService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
        }
    }
    
    // Create a new payment
    public Payment createPayment(Payment payment) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return null;
        }
        
        String query = "INSERT INTO payment (payment_date, payment_method, payment_status, payment_amount) " +
                       "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new java.sql.Date(payment.getPaymentDate().getTime()));
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setString(3, payment.getPaymentStatus());
            pstmt.setDouble(4, payment.getPaymentAmount());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setPaymentId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
            
            return payment;
        } catch (SQLException e) {
            logger.severe("Error creating payment: " + e.getMessage());
            return null;
        }
    }
    
    // Get payment by ID
    public Payment getPaymentById(int paymentId) {
        String query = "SELECT * FROM payment WHERE payment_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                        rs.getInt("payment_id"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status"),
                        rs.getDouble("payment_amount")
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting payment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update payment status
    public boolean updatePaymentStatus(int paymentId, String status) {
        String query = "UPDATE payment SET payment_status = ? WHERE payment_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, paymentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating payment status: " + e.getMessage());
            return false;
        }
    }
    
    // Process payment (example for integrating with a payment gateway)
    public boolean processPayment(Payment payment) {
        // In a real application, this would integrate with a payment gateway
        // For now, we'll simulate a successful payment
        payment.setPaymentStatus("completed");
        
        // Update the payment in the database
        String query = "UPDATE payment SET payment_status = ? WHERE payment_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, payment.getPaymentStatus());
            pstmt.setInt(2, payment.getPaymentId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error processing payment: " + e.getMessage());
            return false;
        }
    }
    
    // Close connection
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
            } catch (SQLException e) {
                logger.severe("Error closing connection: " + e.getMessage());
            }
        }
    }
}