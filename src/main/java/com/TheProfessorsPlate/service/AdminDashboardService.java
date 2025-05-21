package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.User;

public class AdminDashboardService {
    private static final Logger logger = Logger.getLogger(AdminDashboardService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;

    public AdminDashboardService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully for AdminDashboardService");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }

    // Get count of total customers
    public int getTotalCustomers() {
        if (isConnectionError) return 0;

        String query = "SELECT COUNT(*) AS customer_count FROM user WHERE user_role = 'customer'";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("customer_count");
            }
        } catch (SQLException e) {
            logger.severe("Error getting total customers: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Get count of total orders
    public int getTotalOrders() {
        if (isConnectionError) return 0;

        String query = "SELECT COUNT(*) AS order_count FROM orders";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("order_count");
            }
        } catch (SQLException e) {
            logger.severe("Error getting total orders: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Get total revenue
    public double getTotalRevenue() {
        if (isConnectionError) return 0.0;

        String query = "SELECT SUM(payment_amount) AS total_revenue FROM payment WHERE payment_status = 'completed'";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            logger.severe("Error getting total revenue: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // Get recent orders
    public List<Order> getRecentOrders(int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY order_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getString("order_status"),
                        rs.getInt("order_quantity"),
                        rs.getInt("delivery_id"),
                        rs.getInt("payment_id")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    // Get recently registered users
    public List<User> getRecentUsers(int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE user_role = 'customer' ORDER BY user_id DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_password"),
                        rs.getString("user_role"),
                        rs.getString("user_email"),
                        rs.getString("phone_number"),
                        rs.getString("user_image")
                    );
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent users: " + e.getMessage());
        }
        
        return users;
    }
    
    // Get order statistics by status
    public Map<String, Integer> getOrderStatsByStatus() {
        if (isConnectionError) return new HashMap<>();

        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT order_status, COUNT(*) as count FROM orders GROUP BY order_status";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                stats.put(rs.getString("order_status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.severe("Error getting order stats: " + e.getMessage());
        }
        
        return stats;
    }
    
    // Update user role (e.g., promote to admin or demote to customer)
    public boolean updateUserRole(int userId, String newRole) {
        if (isConnectionError) return false;

        String query = "UPDATE user SET user_role = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Error updating user role: " + e.getMessage());
            return false;
        }
    }
    
    // Delete a user (careful with this operation due to foreign key constraints)
    public boolean deleteUser(int userId) {
        if (isConnectionError) return false;

        // First check if there are any orders or other records associated with this user
        String checkQuery = "SELECT COUNT(*) as count FROM cart_details WHERE user_id = ?";
        
        try (PreparedStatement checkStmt = dbConn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, userId);
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt("count") > 0) {
                    logger.warning("Cannot delete user with ID " + userId + " because they have associated records");
                    return false;
                }
            }
            
            // If no associated records, proceed with deletion
            String deleteQuery = "DELETE FROM user WHERE user_id = ?";
            
            try (PreparedStatement deleteStmt = dbConn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, userId);
                
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            logger.severe("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // Close the database connection
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
            } catch (SQLException e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}