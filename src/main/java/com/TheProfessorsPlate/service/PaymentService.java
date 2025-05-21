package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Payment;

public class PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;
    
    public PaymentService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            isConnectionError = false;
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
            isConnectionError = true;
        }
    }
    
    // Create a new payment
    public Payment createPayment(String paymentMethod, BigDecimal amount) {
        if (isConnectionError) return null;
        
        String query = "INSERT INTO payment (payment_date, payment_method, payment_status, payment_amount) " +
                      "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            Date currentDate = new Date();
            pstmt.setDate(1, new java.sql.Date(currentDate.getTime()));
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, "pending"); // Initial status
            pstmt.setBigDecimal(4, amount);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                logger.warning("Creating payment failed, no rows affected.");
                return null;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(generatedKeys.getInt(1));
                    payment.setPaymentDate(currentDate);
                    payment.setPaymentMethod(paymentMethod);
                    payment.setPaymentStatus("pending");
                    payment.setPaymentAmount(amount);
                    return payment;
                } else {
                    logger.warning("Creating payment failed, no ID obtained.");
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating payment: " + e.getMessage());
            return null;
        }
    }
    
    // Get payment by ID
    public Payment getPaymentById(int paymentId) {
        if (isConnectionError) return null;
        
        String query = "SELECT * FROM payment WHERE payment_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setPaymentDate(rs.getDate("payment_date"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setPaymentStatus(rs.getString("payment_status"));
                    payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
                    return payment;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting payment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update payment status
    public boolean updatePaymentStatus(int paymentId, String status) {
        if (isConnectionError) return false;
        
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
    
    // Process payment (simulate payment gateway integration)
    public boolean processPayment(int paymentId, String method) {
        if (isConnectionError) return false;
        
        // In a real-world scenario, this would integrate with a payment gateway
        // For this implementation, we'll simply update the status to "completed"
        
        return updatePaymentStatus(paymentId, "completed");
    }
    
    // Clean up resources
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