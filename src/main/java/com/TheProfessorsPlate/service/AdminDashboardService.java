package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Menu;
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
    
    public int getTotalCustomers() {
        if (isConnectionError) return 0;
        
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM user WHERE user_role = 'customer'";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting total customers: " + e.getMessage());
        }
        
        return count;
    }
    
    public int getTotalOrders() {
        if (isConnectionError) return 0;
        
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM orders";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting total orders: " + e.getMessage());
        }
        
        return count;
    }
    
    public double getTotalRevenue() {
        if (isConnectionError) return 0;
        
        double revenue = 0;
        String query = "SELECT SUM(p.payment_amount) as total FROM payment p " +
                      "JOIN orders o ON p.payment_id = o.payment_id " +
                      "WHERE p.payment_status = 'completed'";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    revenue = rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting total revenue: " + e.getMessage());
        }
        
        return revenue;
    }
    
    public int getTotalMenuItems() {
        if (isConnectionError) return 0;
        
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM menu";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting total menu items: " + e.getMessage());
        }
        
        return count;
    }
    
    public List<Order> getRecentOrders(int limit) {
        if (isConnectionError) return new ArrayList<>();
        
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.order_id, o.order_date, o.order_status, " +
                      "p.payment_amount as order_amount, u.user_name as customer_name " +
                      "FROM orders o " +
                      "JOIN payment p ON o.payment_id = p.payment_id " +
                      "JOIN cart c ON o.order_id = c.order_id " +
                      "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                      "JOIN user u ON cd.user_id = u.user_id " +
                      "GROUP BY o.order_id " +
                      "ORDER BY o.order_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setOrderAmount(rs.getDouble("order_amount"));
                    order.setCustomerName(rs.getString("customer_name"));
                    
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    public List<User> getRecentUsers(int limit) {
        if (isConnectionError) return new ArrayList<>();
        
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, user_name, user_email, user_role " +
                      "FROM user " +
                      "ORDER BY user_id DESC LIMIT ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserEmail(rs.getString("user_email"));
                    user.setUserRole(rs.getString("user_role"));
                    
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent users: " + e.getMessage());
        }
        
        return users;
    }
    
    public List<Menu> getRecentMenuItems(int limit) {
        if (isConnectionError) return new ArrayList<>();
        
        List<Menu> items = new ArrayList<>();
        String query = "SELECT m.*, " +
                      "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                      "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                      "     ELSE m.food_price END AS discounted_price " +
                      "FROM menu m " +
                      "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                      "ORDER BY m.food_id DESC LIMIT ?";
        
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
                    menu.setStockQuantity(rs.getInt("stock_quantity"));
                    
                    items.add(menu);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting recent menu items: " + e.getMessage());
        }
        
        return items;
    }
    
    public Map<String, Integer> getOrderStatsByStatus() {
        if (isConnectionError) return new LinkedHashMap<>();
        
        Map<String, Integer> orderStats = new LinkedHashMap<>();
        String query = "SELECT order_status, COUNT(*) as count FROM orders GROUP BY order_status";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderStats.put(rs.getString("order_status"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting order stats by status: " + e.getMessage());
        }
        
        // Ensure all statuses are represented
        if (!orderStats.containsKey("pending")) orderStats.put("pending", 0);
        if (!orderStats.containsKey("processing")) orderStats.put("processing", 0);
        if (!orderStats.containsKey("shipping")) orderStats.put("shipping", 0);
        if (!orderStats.containsKey("delivered")) orderStats.put("delivered", 0);
        if (!orderStats.containsKey("cancelled")) orderStats.put("cancelled", 0);
        
        return orderStats;
    }
    
    public Map<String, Double> getWeeklySales() {
        if (isConnectionError) return new LinkedHashMap<>();
        
        Map<String, Double> weeklySales = new LinkedHashMap<>();
        String query = "SELECT DATE_FORMAT(o.order_date, '%W') as day, SUM(p.payment_amount) as total " +
                      "FROM orders o " +
                      "JOIN payment p ON o.payment_id = p.payment_id " +
                      "WHERE o.order_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                      "AND p.payment_status = 'completed' " +
                      "GROUP BY DATE_FORMAT(o.order_date, '%W') " +
                      "ORDER BY FIELD(DATE_FORMAT(o.order_date, '%W'), " +
                      "'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday')";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    weeklySales.put(rs.getString("day"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting weekly sales: " + e.getMessage());
        }
        
        // Ensure all days are represented
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : days) {
            if (!weeklySales.containsKey(day)) {
                weeklySales.put(day, 0.0);
            }
        }
        
        return weeklySales;
    }
    
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
                logger.info("Database connection closed for AdminDashboardService");
            } catch (SQLException e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}