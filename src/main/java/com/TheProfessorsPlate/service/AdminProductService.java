package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Discount;
import com.TheProfessorsPlate.model.Menu;

public class AdminProductService {
    private static final Logger logger = Logger.getLogger(AdminProductService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;
    
    public AdminProductService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully for AdminProductService");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }
    
    public List<Menu> getAllMenuItems() {
        if (isConnectionError) return new ArrayList<>();
        
        List<Menu> menuItems = new ArrayList<>();
        String query = "SELECT m.*, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM menu m " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                       "ORDER BY m.food_id DESC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
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
                    
                    menuItems.add(menu);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting all menu items: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    public Menu getMenuItemById(int foodId) {
        if (isConnectionError) return null;
        
        Menu menu = null;
        String query = "SELECT m.*, " +
                      "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                      "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                      "     ELSE m.food_price END AS discounted_price " +
                      "FROM menu m " +
                      "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                      "WHERE m.food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, foodId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    menu = new Menu();
                    menu.setFoodId(rs.getInt("food_id"));
                    menu.setFoodName(rs.getString("food_name"));
                    menu.setFoodDescription(rs.getString("food_description"));
                    menu.setFoodPrice(rs.getBigDecimal("food_price"));
                    menu.setFoodCategory(rs.getString("food_category"));
                    menu.setFoodImage(rs.getString("food_image"));
                    menu.setDiscountId(rs.getInt("discount_id"));
                    menu.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    menu.setStockQuantity(rs.getInt("stock_quantity"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting menu item by ID: " + e.getMessage());
        }
        
        return menu;
    }
    
    public boolean addMenuItem(Menu menu) {
        if (isConnectionError) return false;
        
        String query = "INSERT INTO menu (food_name, food_description, food_category, food_price, discount_id, food_image, stock_quantity) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, menu.getFoodName());
            pstmt.setString(2, menu.getFoodDescription());
            pstmt.setString(3, menu.getFoodCategory());
            pstmt.setBigDecimal(4, menu.getFoodPrice());
            pstmt.setInt(5, menu.getDiscountId());
            pstmt.setString(6, menu.getFoodImage());
            pstmt.setInt(7, menu.getStockQuantity());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Error adding menu item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateMenuItem(Menu menu) {
        if (isConnectionError) return false;
        
        String query = "UPDATE menu SET food_name = ?, food_description = ?, food_category = ?, " +
                      "food_price = ?, discount_id = ?, food_image = ?, stock_quantity = ? " +
                      "WHERE food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, menu.getFoodName());
            pstmt.setString(2, menu.getFoodDescription());
            pstmt.setString(3, menu.getFoodCategory());
            pstmt.setBigDecimal(4, menu.getFoodPrice());
            pstmt.setInt(5, menu.getDiscountId());
            pstmt.setString(6, menu.getFoodImage());
            pstmt.setInt(7, menu.getStockQuantity());
            pstmt.setInt(8, menu.getFoodId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Error updating menu item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMenuItem(int foodId) {
        if (isConnectionError) return false;
        
        String query = "DELETE FROM menu WHERE food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, foodId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> getAllCategories() {
        if (isConnectionError) return new ArrayList<>();
        
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT food_category FROM menu ORDER BY food_category";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("food_category"));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting all categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    public List<Discount> getAllDiscounts() {
        if (isConnectionError) return new ArrayList<>();
        
        List<Discount> discounts = new ArrayList<>();
        String query = "SELECT * FROM discount ORDER BY discount_id";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Discount discount = new Discount();
                    discount.setDiscountId(rs.getInt("discount_id"));
                    discount.setDiscountName(rs.getString("discount_name"));
                    discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                    discount.setDiscountAmount(rs.getDouble("discount_amount"));
                    
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting all discounts: " + e.getMessage());
        }
        
        return discounts;
    }
    
    public boolean updateStockQuantity(int foodId, int quantity) {
        if (isConnectionError) return false;
        
        String query = "UPDATE menu SET stock_quantity = ? WHERE food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, foodId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Error updating stock quantity: " + e.getMessage());
            return false;
        }
    }
    
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
                logger.info("Database connection closed for AdminProductService");
            } catch (SQLException e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}