package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscountService {
    private static final Logger logger = Logger.getLogger(DiscountService.class.getName());
    private Connection dbConn;
    
    public DiscountService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
        }
    }
    
    // Create a new discount
    public Discount createDiscount(Discount discount) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return null;
        }
        
        String query = "INSERT INTO discount (discount_name, discount_percentage, discount_amount, discount_period) " +
                       "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, discount.getDiscountName());
            pstmt.setDouble(2, discount.getDiscountPercentage());
            pstmt.setDouble(3, discount.getDiscountAmount());
            pstmt.setDate(4, new java.sql.Date(discount.getDiscountPeriod().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating discount failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discount.setDiscountId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating discount failed, no ID obtained.");
                }
            }
            
            return discount;
        } catch (SQLException e) {
            logger.severe("Error creating discount: " + e.getMessage());
            return null;
        }
    }
    
    // Get discount by ID
    public Discount getDiscountById(int discountId) {
        String query = "SELECT * FROM discount WHERE discount_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, discountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Discount(
                        rs.getInt("discount_id"),
                        rs.getString("discount_name"),
                        rs.getDouble("discount_percentage"),
                        rs.getDouble("discount_amount"),
                        rs.getDate("discount_period")
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting discount by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all discounts
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        String query = "SELECT * FROM discount";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Discount discount = new Discount(
                    rs.getInt("discount_id"),
                    rs.getString("discount_name"),
                    rs.getDouble("discount_percentage"),
                    rs.getDouble("discount_amount"),
                    rs.getDate("discount_period")
                );
                discounts.add(discount);
            }
        } catch (SQLException e) {
            logger.severe("Error getting all discounts: " + e.getMessage());
        }
        
        return discounts;
    }
    
    // Update discount
    public boolean updateDiscount(Discount discount) {
        String query = "UPDATE discount SET discount_name = ?, discount_percentage = ?, " +
                       "discount_amount = ?, discount_period = ? WHERE discount_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, discount.getDiscountName());
            pstmt.setDouble(2, discount.getDiscountPercentage());
            pstmt.setDouble(3, discount.getDiscountAmount());
            pstmt.setDate(4, new java.sql.Date(discount.getDiscountPeriod().getTime()));
            pstmt.setInt(5, discount.getDiscountId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating discount: " + e.getMessage());
            return false;
        }
    }
    
    // Delete discount
    public boolean deleteDiscount(int discountId) {
        String query = "DELETE FROM discount WHERE discount_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, discountId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting discount: " + e.getMessage());
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