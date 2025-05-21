package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.Review;

public class CustomerDashboardService {
    private static final Logger logger = Logger.getLogger(CustomerDashboardService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;

    public CustomerDashboardService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully for CustomerDashboardService");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }

    // Get recent orders for a specific customer
    public List<Order> getRecentOrdersByUserId(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.* FROM orders o " +
                       "JOIN cart c ON o.order_id = c.order_id " + 
                       "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                       "WHERE cd.user_id = ? " +
                       "ORDER BY o.order_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
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
            logger.severe("Error getting recent orders by user ID: " + e.getMessage());
        }
        
        return orders;
    }
    
    // Get favorite food items
    public List<Menu> getFavoriteMenuItems(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<Menu> favorites = new ArrayList<>();
        String query = "SELECT m.*, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM menu m " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                       "JOIN menu_details md ON m.food_id = md.food_id " +
                       "WHERE md.user_id = ? " +
                       "LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setFoodId(rs.getInt("food_id"));
                    menu.setFoodName(rs.getString("food_name"));
                    menu.setFoodDescription(rs.getString("food_description"));
                    menu.setFoodPrice(rs.getBigDecimal("food_price"));
                    menu.setFoodCategory(rs.getString("food_category"));
                    menu.setFoodImage(rs.getString("food_image"));
                    menu.setDiscountId(rs.getInt("discount_id"));
                    menu.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    
                    favorites.add(menu);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting favorite menu items: " + e.getMessage());
        }
        
        return favorites;
    }
    
    // Get recommended menu items based on user's previous orders
    public List<Menu> getRecommendedMenuItems(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();

        // Get categories the user has ordered before
        List<String> userCategories = new ArrayList<>();
        String categoryQuery = "SELECT DISTINCT m.food_category FROM menu m " +
                              "JOIN cart_details cd ON m.food_id = cd.food_id " +
                              "WHERE cd.user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(categoryQuery)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userCategories.add(rs.getString("food_category"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting user categories: " + e.getMessage());
            return new ArrayList<>();
        }
        
        // If user has no previous orders, return random menu items
        if (userCategories.isEmpty()) {
            return getRandomMenuItems(limit);
        }
        
        // Get menu items in the same categories that the user hasn't ordered before
        List<Menu> recommendations = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT m.*, " +
            "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
            "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
            "     ELSE m.food_price END AS discounted_price " +
            "FROM menu m " +
            "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
            "WHERE m.food_category IN ("
        );
        
        // Add placeholders for each category
        for (int i = 0; i < userCategories.size(); i++) {
            queryBuilder.append("?");
            if (i < userCategories.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        
        queryBuilder.append(") AND m.food_id NOT IN (" +
            "SELECT cd.food_id FROM cart_details cd WHERE cd.user_id = ?" +
        ") ORDER BY RAND() LIMIT ?");
        
        String query = queryBuilder.toString();
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            // Set categories
            for (int i = 0; i < userCategories.size(); i++) {
                pstmt.setString(i + 1, userCategories.get(i));
            }
            
            // Set user_id and limit
            pstmt.setInt(userCategories.size() + 1, userId);
            pstmt.setInt(userCategories.size() + 2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setFoodId(rs.getInt("food_id"));
                    menu.setFoodName(rs.getString("food_name"));
                    menu.setFoodDescription(rs.getString("food_description"));
                    menu.setFoodPrice(rs.getBigDecimal("food_price"));
                    menu.setFoodCategory(rs.getString("food_category"));
                    menu.setFoodImage(rs.getString("food_image"));
                    menu.setDiscountId(rs.getInt("discount_id"));
                    menu.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    
                    recommendations.add(menu);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recommended menu items: " + e.getMessage());
            return getRandomMenuItems(limit); // Fallback to random items
        }
        
        // If we didn't get enough recommendations, fill with random items
        if (recommendations.size() < limit) {
            int remaining = limit - recommendations.size();
            recommendations.addAll(getRandomMenuItems(remaining));
        }
        
        return recommendations;
    }
    
    // Helper method to get random menu items
    private List<Menu> getRandomMenuItems(int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<Menu> randomItems = new ArrayList<>();
        String query = "SELECT m.*, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM menu m " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                       "ORDER BY RAND() LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setFoodId(rs.getInt("food_id"));
                    menu.setFoodName(rs.getString("food_name"));
                    menu.setFoodDescription(rs.getString("food_description"));
                    menu.setFoodPrice(rs.getBigDecimal("food_price"));
                    menu.setFoodCategory(rs.getString("food_category"));
                    menu.setFoodImage(rs.getString("food_image"));
                    menu.setDiscountId(rs.getInt("discount_id"));
                    menu.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    
                    randomItems.add(menu);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting random menu items: " + e.getMessage());
        }
        
        return randomItems;
    }
    
    // Get recent reviews by the user
    public List<Review> getRecentReviewsByUserId(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();

        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.* " +
                      "FROM review r " +
                      "JOIN review_details rd ON r.review_id = rd.review_id " +
                      "WHERE rd.user_id = ? " +
                      "ORDER BY r.review_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review(
                        rs.getInt("review_id"),
                        rs.getDouble("rating"),
                        rs.getString("review_description"),
                        rs.getTimestamp("review_date").toString()
                    );
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent reviews by user ID: " + e.getMessage());
        }
        
        return reviews;
    }
    
    // Get total spent by user
    public double getTotalSpentByUser(int userId) {
        if (isConnectionError) return 0.0;

        String query = "SELECT SUM(p.payment_amount) AS total_spent " +
                      "FROM payment p " +
                      "JOIN orders o ON p.payment_id = o.payment_id " +
                      "JOIN cart c ON o.order_id = c.order_id " +
                      "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                      "WHERE cd.user_id = ? AND p.payment_status = 'completed'";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_spent");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting total spent by user: " + e.getMessage());
        }
        
        return 0.0;
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