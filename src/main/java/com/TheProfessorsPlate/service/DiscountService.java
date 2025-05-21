package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscountService {
    private static final Logger logger = Logger.getLogger(DiscountService.class.getName());
    private Connection dbConn;
    
    public DiscountService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established for DiscountService");
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
            
            // Safely handle null discount period
            if (discount.getDiscountPeriod() != null) {
                pstmt.setDate(4, new java.sql.Date(discount.getDiscountPeriod().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating discount failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discount.setDiscountId(generatedKeys.getInt(1));
                    logger.info("Created new discount with ID: " + discount.getDiscountId());
                } else {
                    throw new SQLException("Creating discount failed, no ID obtained.");
                }
            }
            
            return discount;
        } catch (SQLException e) {
            logger.severe("Error creating discount: " + e.getMessage());
            e.printStackTrace();
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
                    Discount discount = new Discount();
                    discount.setDiscountId(rs.getInt("discount_id"));
                    discount.setDiscountName(rs.getString("discount_name"));
                    discount.setDiscountPercentage(rs.getDouble("discount_percentage"));
                    discount.setDiscountAmount(rs.getDouble("discount_amount"));
                    
                    // Safely handle null date
                    java.sql.Date sqlDate = rs.getDate("discount_period");
                    if (sqlDate != null) {
                        discount.setDiscountPeriod(new java.util.Date(sqlDate.getTime()));
                    }
                    
                    return discount;
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
        String query = "SELECT * FROM discount ORDER BY discount_id DESC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(rs.getInt("discount_id"));
                discount.setDiscountName(rs.getString("discount_name"));
                discount.setDiscountPercentage(rs.getDouble("discount_percentage"));
                discount.setDiscountAmount(rs.getDouble("discount_amount"));
                
                // Safely handle null date
                java.sql.Date sqlDate = rs.getDate("discount_period");
                if (sqlDate != null && !rs.wasNull()) {
                    discount.setDiscountPeriod(new java.util.Date(sqlDate.getTime()));
                }
                
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
            
            // Safely handle null date
            if (discount.getDiscountPeriod() != null) {
                pstmt.setDate(4, new java.sql.Date(discount.getDiscountPeriod().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            pstmt.setInt(5, discount.getDiscountId());
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                logger.info("Updated discount ID: " + discount.getDiscountId());
            }
            return result > 0;
        } catch (SQLException e) {
            logger.severe("Error updating discount: " + e.getMessage());
            return false;
        }
    }
    
    // Delete discount
    public boolean deleteDiscount(int discountId) {
        // First check if this discount is being used
        if (isDiscountInUse(discountId)) {
            logger.warning("Cannot delete discount ID " + discountId + " because it is in use by menu items");
            return false;
        }
        
        String query = "DELETE FROM discount WHERE discount_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, discountId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                logger.info("Deleted discount ID: " + discountId);
            }
            return result > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting discount: " + e.getMessage());
            return false;
        }
    }
    
    // Check if discount is being used by any menu items
    private boolean isDiscountInUse(int discountId) {
        String query = "SELECT COUNT(*) FROM menu WHERE discount_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, discountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking if discount is in use: " + e.getMessage());
        }
        
        return false;
    }
    
    // Get active discounts (not expired)
    public List<Discount> getActiveDiscounts() {
        List<Discount> activeDiscounts = new ArrayList<>();
        String query = "SELECT * FROM discount WHERE discount_period IS NULL OR discount_period >= CURRENT_DATE()";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(rs.getInt("discount_id"));
                discount.setDiscountName(rs.getString("discount_name"));
                discount.setDiscountPercentage(rs.getDouble("discount_percentage"));
                discount.setDiscountAmount(rs.getDouble("discount_amount"));
                
                // Safely handle null date
                java.sql.Date sqlDate = rs.getDate("discount_period");
                if (sqlDate != null && !rs.wasNull()) {
                    discount.setDiscountPeriod(new java.util.Date(sqlDate.getTime()));
                }
                
                activeDiscounts.add(discount);
            }
        } catch (SQLException e) {
            logger.severe("Error getting active discounts: " + e.getMessage());
        }
        
        return activeDiscounts;
    }
    
    // Close connection
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
                logger.info("Database connection closed for DiscountService");
            } catch (SQLException e) {
                logger.severe("Error closing connection: " + e.getMessage());
            }
        }
    }
}