package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.CartDetail;
import com.TheProfessorsPlate.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CartService {
    private static final Logger logger = Logger.getLogger(CartService.class.getName());
    private Connection dbConn;
    
    public CartService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
        }
    }

    // Create a new cart
    public Cart createCart(Cart cart) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return null;
        }
        
        String query = "INSERT INTO cart (count, total_price, order_id, delivery_id, payment_id) " +
                      "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, cart.getCount());
            pstmt.setDouble(2, cart.getTotalPrice());
            pstmt.setInt(3, cart.getOrderId());
            pstmt.setInt(4, cart.getDeliveryId());
            pstmt.setInt(5, cart.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating cart failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cart.setCartId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating cart failed, no ID obtained.");
                }
            }
            
            return cart;
        } catch (SQLException e) {
            logger.severe("Error creating cart: " + e.getMessage());
            return null;
        }
    }
    
    // Add item to cart
    public boolean addToCart(int userId, int foodId, int cartId) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return false;
        }
        
        String query = "INSERT INTO cart_details (user_id, food_id, cart_id) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, foodId);
            pstmt.setInt(3, cartId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error adding item to cart: " + e.getMessage());
            return false;
        }
    }
    
    // Get cart by ID
    public Cart getCartById(int cartId) {
        String query = "SELECT * FROM cart WHERE cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cart(
                        rs.getInt("cart_id"),
                        rs.getInt("count"),
                        rs.getDouble("total_price"),
                        rs.getInt("order_id"),
                        rs.getInt("delivery_id"),
                        rs.getInt("payment_id")
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting cart by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get items in a cart
    public List<Menu> getCartItems(int cartId) {
        List<Menu> items = new ArrayList<>();
        String query = "SELECT m.* FROM menu m " +
                       "JOIN cart_details cd ON m.food_id = cd.food_id " +
                       "WHERE cd.cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Menu item = new Menu(
                        rs.getInt("food_id"),
                        rs.getString("food_name"),
                        rs.getString("food_description"),
                        rs.getDouble("food_price"),
                        rs.getString("food_category"),
                        rs.getString("food_image"),
                        rs.getInt("discount_id")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting cart items: " + e.getMessage());
        }
        
        return items;
    }
    
    // Update cart
    public boolean updateCart(Cart cart) {
        String query = "UPDATE cart SET count = ?, total_price = ?, order_id = ?, " +
                      "delivery_id = ?, payment_id = ? WHERE cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, cart.getCount());
            pstmt.setDouble(2, cart.getTotalPrice());
            pstmt.setInt(3, cart.getOrderId());
            pstmt.setInt(4, cart.getDeliveryId());
            pstmt.setInt(5, cart.getPaymentId());
            pstmt.setInt(6, cart.getCartId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating cart: " + e.getMessage());
            return false;
        }
    }
    
    // Remove item from cart
    public boolean removeFromCart(int userId, int foodId, int cartId) {
        String query = "DELETE FROM cart_details WHERE user_id = ? AND food_id = ? AND cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, foodId);
            pstmt.setInt(3, cartId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error removing item from cart: " + e.getMessage());
            return false;
        }
    }
    
    // Delete cart
    public boolean deleteCart(int cartId) {
        // First delete cart details (foreign key constraint)
        String deleteDetailsQuery = "DELETE FROM cart_details WHERE cart_id = ?";
        String deleteCartQuery = "DELETE FROM cart WHERE cart_id = ?";
        
        try {
            dbConn.setAutoCommit(false);
            
            try (PreparedStatement detailsStmt = dbConn.prepareStatement(deleteDetailsQuery)) {
                detailsStmt.setInt(1, cartId);
                detailsStmt.executeUpdate();
            }
            
            try (PreparedStatement cartStmt = dbConn.prepareStatement(deleteCartQuery)) {
                cartStmt.setInt(1, cartId);
                int result = cartStmt.executeUpdate();
                
                dbConn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            try {
                dbConn.rollback();
            } catch (SQLException ex) {
                logger.severe("Error rolling back transaction: " + ex.getMessage());
            }
            logger.severe("Error deleting cart: " + e.getMessage());
            return false;
        } finally {
            try {
                dbConn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.severe("Error resetting auto-commit: " + e.getMessage());
            }
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