package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    
    // NEW METHODS FOR ENHANCED DASHBOARD
    
    // Get order count by status
    public Map<String, Integer> getOrderCountByStatus(int userId) {
        if (isConnectionError) return new HashMap<>();
        
        Map<String, Integer> orderCounts = new HashMap<>();
        String query = "SELECT o.order_status, COUNT(*) as count FROM orders o " +
                       "JOIN cart c ON o.order_id = c.order_id " +
                       "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                       "WHERE cd.user_id = ? " +
                       "GROUP BY o.order_status";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderCounts.put(rs.getString("order_status"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting order count by status: " + e.getMessage());
        }
        
        return orderCounts;
    }
    
    // Get monthly spending data for the past 6 months
    public Map<String, Double> getMonthlySpending(int userId) {
        if (isConnectionError) return new HashMap<>();
        
        Map<String, Double> monthlySpending = new LinkedHashMap<>(); // Maintain insertion order
        String query = "SELECT DATE_FORMAT(o.order_date, '%Y-%m') as month, " +
                       "SUM(p.payment_amount) as total " +
                       "FROM orders o " +
                       "JOIN payment p ON o.payment_id = p.payment_id " +
                       "JOIN cart c ON o.order_id = c.order_id " +
                       "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                       "WHERE cd.user_id = ? " +
                       "AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                       "GROUP BY DATE_FORMAT(o.order_date, '%Y-%m') " +
                       "ORDER BY month ASC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String month = rs.getString("month");
                    double total = rs.getDouble("total");
                    monthlySpending.put(formatMonthLabel(month), total);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting monthly spending: " + e.getMessage());
        }
        
        // Make sure we have entries for all 6 months even if there was no spending
        fillMissingMonths(monthlySpending);
        
        return monthlySpending;
    }
    
    // Get spending by food category
    public Map<String, Double> getSpendingByCategory(int userId) {
        if (isConnectionError) return new HashMap<>();
        
        Map<String, Double> categorySpending = new HashMap<>();
        String query = "SELECT m.food_category, SUM(cd.quantity * m.food_price) as total " +
                       "FROM cart_details cd " +
                       "JOIN menu m ON cd.food_id = m.food_id " +
                       "JOIN cart c ON cd.cart_id = c.cart_id " +
                       "JOIN orders o ON c.order_id = o.order_id " +
                       "WHERE cd.user_id = ? " +
                       "AND o.order_status != 'cancelled' " +
                       "GROUP BY m.food_category";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("food_category");
                    double total = rs.getDouble("total");
                    categorySpending.put(category, total);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting spending by category: " + e.getMessage());
        }
        
        return categorySpending;
    }
    
    // Get recent activity (orders, reviews, etc.)
    public List<Map<String, Object>> getRecentActivity(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();
        
        List<Map<String, Object>> activities = new ArrayList<>();
        
        // Get recent orders
        String orderQuery = "SELECT o.order_id, o.order_date, 'order' as type, " +
                            "CONCAT('Order #', o.order_id, ' (', o.order_status, ')') as description " +
                            "FROM orders o " +
                            "JOIN cart c ON o.order_id = c.order_id " +
                            "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                            "WHERE cd.user_id = ? " +
                            "ORDER BY o.order_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(orderQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("id", rs.getInt("order_id"));
                    activity.put("date", rs.getTimestamp("order_date"));
                    activity.put("type", rs.getString("type"));
                    activity.put("description", rs.getString("description"));
                    activities.add(activity);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent order activity: " + e.getMessage());
        }
        
        // Get recent reviews
        String reviewQuery = "SELECT r.review_id, r.review_date, 'review' as type, " +
                             "CONCAT('Reviewed with rating ', r.rating, '/5') as description " +
                             "FROM review r " +
                             "JOIN review_details rd ON r.review_id = rd.review_id " +
                             "WHERE rd.user_id = ? " +
                             "ORDER BY r.review_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(reviewQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("id", rs.getInt("review_id"));
                    activity.put("date", rs.getTimestamp("review_date"));
                    activity.put("type", rs.getString("type"));
                    activity.put("description", rs.getString("description"));
                    activities.add(activity);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent review activity: " + e.getMessage());
        }
        
        // Sort all activities by date (most recent first)
        activities.sort((a1, a2) -> ((Timestamp)a2.get("date")).compareTo((Timestamp)a1.get("date")));
        
        // Limit to the requested number
        return activities.size() <= limit ? activities : activities.subList(0, limit);
    }
    
    // Get most ordered items
    public List<Map<String, Object>> getMostOrderedItems(int userId, int limit) {
        if (isConnectionError) return new ArrayList<>();
        
        List<Map<String, Object>> items = new ArrayList<>();
        String query = "SELECT m.food_id, m.food_name, m.food_image, SUM(cd.quantity) as total_ordered " +
                      "FROM cart_details cd " +
                      "JOIN menu m ON cd.food_id = m.food_id " +
                      "WHERE cd.user_id = ? " +
                      "GROUP BY m.food_id, m.food_name, m.food_image " +
                      "ORDER BY total_ordered DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("foodId", rs.getInt("food_id"));
                    item.put("foodName", rs.getString("food_name"));
                    item.put("foodImage", rs.getString("food_image"));
                    item.put("orderCount", rs.getInt("total_ordered"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting most ordered items: " + e.getMessage());
        }
        
        return items;
    }
    
    // Helper method to format month labels (e.g. "2025-05" to "May 2025")
    private String formatMonthLabel(String yearMonth) {
        try {
            String[] parts = yearMonth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            
            String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            
            return monthNames[month - 1] + " " + year;
        } catch (Exception e) {
            return yearMonth; // Return as is if parsing fails
        }
    }
    
    // Helper method to ensure we have entries for the past 6 months
    private void fillMissingMonths(Map<String, Double> monthlySpending) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");
        
        // Create entries for the past 6 months
        for (int i = 5; i >= 0; i--) {
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -i);
            String yearMonth = yearMonthFormat.format(cal.getTime());
            String formattedMonth = formatMonthLabel(yearMonth);
            
            if (!monthlySpending.containsKey(formattedMonth)) {
                monthlySpending.put(formattedMonth, 0.0);
            }
        }
        
        // Sort the map by month
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        monthlySpending.entrySet()
            .stream()
            .sorted((e1, e2) -> {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
                    Date d1 = sdf.parse(e1.getKey());
                    Date d2 = sdf.parse(e2.getKey());
                    return d1.compareTo(d2);
                } catch (Exception e) {
                    return 0;
                }
            })
            .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
        
        monthlySpending.clear();
        monthlySpending.putAll(sortedMap);
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