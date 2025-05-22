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
import com.TheProfessorsPlate.model.Menu;

public class MenuService {
    private static final Logger logger = Logger.getLogger(MenuService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;

    public MenuService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully for MenuService");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }

    // Create a new menu item
    public Integer addMenuItem(Menu menu) {
        if (isConnectionError) return null;

        String query = "INSERT INTO menu (food_name, food_description, food_price, food_category, food_image, discount_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, menu.getFoodName());
            pstmt.setString(2, menu.getFoodDescription());
            pstmt.setBigDecimal(3, menu.getFoodPrice());
            pstmt.setString(4, menu.getFoodCategory());
            pstmt.setString(5, menu.getFoodImage());
            pstmt.setInt(6, menu.getDiscountId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                logger.warning("Creating menu item failed, no rows affected.");
                return null;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    menu.setFoodId(id);
                    logger.info("Menu item added successfully with ID: " + id);
                    return id;
                } else {
                    logger.warning("Creating menu item failed, no ID obtained.");
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error adding menu item: " + e.getMessage());
            return null;
        }
    }
    
    // Read all menu items
    public List<Menu> getAllMenuItems() {
        if (isConnectionError) return new ArrayList<>();

        List<Menu> menuList = new ArrayList<>();
        String query = "SELECT m.*, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM menu m " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
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
                
                menuList.add(menu);
            }
            
            logger.info("Retrieved " + menuList.size() + " menu items");
        } catch (SQLException e) {
            logger.severe("Error retrieving menu items: " + e.getMessage());
        }
        
        return menuList;
    }
    
    // Get menu items by category
    public List<Menu> getMenuItemsByCategory(String category) {
        if (isConnectionError) return new ArrayList<>();

        List<Menu> menuList = new ArrayList<>();
        String query = "SELECT m.*, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM menu m " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                       "WHERE m.food_category = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, category);
            
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
                    
                    menuList.add(menu);
                }
            }
            
            logger.info("Retrieved " + menuList.size() + " menu items for category: " + category);
        } catch (SQLException e) {
            logger.severe("Error retrieving menu items by category: " + e.getMessage());
        }
        
        return menuList;
    }
    
    // Get menu item by ID
    public Menu getMenuItemById(int foodId) {
        if (isConnectionError) return null;

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
                    Menu menu = new Menu();
                    menu.setFoodId(rs.getInt("food_id"));
                    menu.setFoodName(rs.getString("food_name"));
                    menu.setFoodDescription(rs.getString("food_description"));
                    menu.setFoodPrice(rs.getBigDecimal("food_price"));
                    menu.setFoodCategory(rs.getString("food_category"));
                    menu.setFoodImage(rs.getString("food_image"));
                    menu.setDiscountId(rs.getInt("discount_id"));
                    menu.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    
                    logger.info("Retrieved menu item with ID: " + foodId);
                    return menu;
                }
            }
            
            logger.warning("Menu item with ID: " + foodId + " not found");
        } catch (SQLException e) {
            logger.severe("Error retrieving menu item by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update menu item
    public boolean updateMenuItem(Menu menu) {
        if (isConnectionError) return false;

        String query = "UPDATE menu SET food_name = ?, food_description = ?, food_price = ?, " +
                       "food_category = ?, food_image = ?, discount_id = ? WHERE food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, menu.getFoodName());
            pstmt.setString(2, menu.getFoodDescription());
            pstmt.setBigDecimal(3, menu.getFoodPrice());
            pstmt.setString(4, menu.getFoodCategory());
            pstmt.setString(5, menu.getFoodImage());
            pstmt.setInt(6, menu.getDiscountId());
            pstmt.setInt(7, menu.getFoodId());
            
            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Menu item with ID: " + menu.getFoodId() + " updated successfully");
            } else {
                logger.warning("Menu item with ID: " + menu.getFoodId() + " not found for update");
            }
            
            return success;
        } catch (SQLException e) {
            logger.severe("Error updating menu item: " + e.getMessage());
            return false;
        }
    }
    
    // Delete menu item
    public boolean deleteMenuItem(int foodId) {
        if (isConnectionError) return false;

        String query = "DELETE FROM menu WHERE food_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, foodId);
            
            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Menu item with ID: " + foodId + " deleted successfully");
            } else {
                logger.warning("Menu item with ID: " + foodId + " not found for deletion");
            }
            
            return success;
        } catch (SQLException e) {
            logger.severe("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }
    
    public void close() {
        if (dbConn != null) {
            try {
                dbConn.close();
                System.out.println("Database connection closed successfully");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}