package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Delivery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DeliveryService {
    private static final Logger logger = Logger.getLogger(DeliveryService.class.getName());
    private Connection dbConn;
    
    public DeliveryService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
        }
    }
    
    // Create a new delivery
    public Delivery createDelivery(Delivery delivery) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return null;
        }
        
        String query = "INSERT INTO delivery (delivery_person, delivery_status, delivery_phone, " +
                       "delivery_time, delivery_location, payment_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, delivery.getDeliveryPerson());
            pstmt.setString(2, delivery.getDeliveryStatus());
            pstmt.setString(3, delivery.getDeliveryPhone());
            pstmt.setDate(4, new java.sql.Date(delivery.getDeliveryTime().getTime()));
            pstmt.setString(5, delivery.getDeliveryLocation());
            pstmt.setInt(6, delivery.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating delivery failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    delivery.setDeliveryId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating delivery failed, no ID obtained.");
                }
            }
            
            return delivery;
        } catch (SQLException e) {
            logger.severe("Error creating delivery: " + e.getMessage());
            return null;
        }
    }
    
    // Get delivery by ID
    public Delivery getDeliveryById(int deliveryId) {
        String query = "SELECT * FROM delivery WHERE delivery_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, deliveryId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Delivery(
                        rs.getInt("delivery_id"),
                        rs.getString("delivery_person"),
                        rs.getString("delivery_status"),
                        rs.getString("delivery_phone"),
                        rs.getDate("delivery_time"),
                        rs.getString("delivery_location"),
                        rs.getInt("payment_id")
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting delivery by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update delivery status
    public boolean updateDeliveryStatus(int deliveryId, String status) {
        String query = "UPDATE delivery SET delivery_status = ? WHERE delivery_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, deliveryId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating delivery status: " + e.getMessage());
            return false;
        }
    }
    
    // Update delivery details
    public boolean updateDelivery(Delivery delivery) {
        String query = "UPDATE delivery SET delivery_person = ?, delivery_status = ?, " +
                       "delivery_phone = ?, delivery_time = ?, delivery_location = ?, " +
                       "payment_id = ? WHERE delivery_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, delivery.getDeliveryPerson());
            pstmt.setString(2, delivery.getDeliveryStatus());
            pstmt.setString(3, delivery.getDeliveryPhone());
            pstmt.setDate(4, new java.sql.Date(delivery.getDeliveryTime().getTime()));
            pstmt.setString(5, delivery.getDeliveryLocation());
            pstmt.setInt(6, delivery.getPaymentId());
            pstmt.setInt(7, delivery.getDeliveryId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating delivery: " + e.getMessage());
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