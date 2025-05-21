package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.CartItem;

public class CartService {
    private static final Logger logger = Logger.getLogger(CartService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;
    
    public CartService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            isConnectionError = false;
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
            isConnectionError = true;
        }
    }

    // Create a new cart
    public Cart createCart(int userId) {
        if (isConnectionError) return null;
        
        String query = "INSERT INTO cart (count, total_price, order_id, delivery_id, payment_id) " +
                      "VALUES (0, 0.00, NULL, NULL, NULL)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating cart failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int cartId = generatedKeys.getInt(1);
                    Cart cart = new Cart();
                    cart.setCartId(cartId);
                    cart.setCount(0);
                    cart.setTotalPrice(BigDecimal.ZERO);
                    cart.setOrderId(0);  // For object representation only
                    cart.setDeliveryId(0);
                    cart.setPaymentId(0);
                    
                    return cart;
                } else {
                    throw new SQLException("Creating cart failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating cart: " + e.getMessage());
            return null;
        }
    }
    
    // Add item to cart
    public boolean addToCart(int userId, int foodId, int cartId) {
        if (isConnectionError) return false;
        
        // First check if the item is already in the cart
        String checkQuery = "SELECT * FROM cart_details WHERE user_id = ? AND food_id = ? AND cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(checkQuery)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, foodId);
            pstmt.setInt(3, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Item already exists in cart, update quantity
                    return true;
                } else {
                    // Item doesn't exist, insert new entry
                    String insertQuery = "INSERT INTO cart_details (user_id, food_id, cart_id) VALUES (?, ?, ?)";
                    
                    try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, foodId);
                        insertStmt.setInt(3, cartId);
                        
                        int result = insertStmt.executeUpdate();
                        
                        // Update the cart count and total price
                        if (result > 0) {
                            updateCartTotals(cartId);
                        }
                        
                        return result > 0;
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error adding item to cart: " + e.getMessage());
            return false;
        }
    }
    
    // Remove item from cart
    public boolean removeFromCart(int userId, int foodId, int cartId) {
        if (isConnectionError) return false;
        
        String query = "DELETE FROM cart_details WHERE user_id = ? AND food_id = ? AND cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, foodId);
            pstmt.setInt(3, cartId);
            
            int result = pstmt.executeUpdate();
            
            // Update the cart count and total price
            if (result > 0) {
                updateCartTotals(cartId);
            }
            
            return result > 0;
        } catch (SQLException e) {
            logger.severe("Error removing item from cart: " + e.getMessage());
            return false;
        }
    }
    
    // Get cart by ID
    public Cart getCartById(int cartId) {
        if (isConnectionError) return null;
        
        String query = "SELECT * FROM cart WHERE cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("cart_id"));
                    cart.setCount(rs.getInt("count"));
                    cart.setTotalPrice(rs.getBigDecimal("total_price"));
                    cart.setOrderId(rs.getInt("order_id"));
                    cart.setDeliveryId(rs.getInt("delivery_id"));
                    cart.setPaymentId(rs.getInt("payment_id"));
                    return cart;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting cart by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get cart items
    public List<CartItem> getCartItems(int cartId) {
        if (isConnectionError) return new ArrayList<>();
        
        List<CartItem> items = new ArrayList<>();
        String query = "SELECT cd.*, m.food_name, m.food_description, m.food_price, m.food_image, " +
                       "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                       "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                       "     ELSE m.food_price END AS discounted_price " +
                       "FROM cart_details cd " +
                       "JOIN menu m ON cd.food_id = m.food_id " +
                       "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                       "WHERE cd.cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setUserId(rs.getInt("user_id"));
                    item.setFoodId(rs.getInt("food_id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setQuantity(1); // Default to 1 since your schema doesn't have quantity
                    
                    // Set additional product details
                    item.setFoodName(rs.getString("food_name"));
                    item.setFoodDescription(rs.getString("food_description"));
                    item.setFoodPrice(rs.getBigDecimal("food_price"));
                    item.setFoodImage(rs.getString("food_image"));
                    item.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                    
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting cart items: " + e.getMessage());
        }
        
        return items;
    }
    
    // Get active cart ID for user
    public Integer getActiveCartId(int userId) {
        if (isConnectionError) return null;
        
        String query = "SELECT DISTINCT c.cart_id FROM cart c " +
                      "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                      "WHERE cd.user_id = ? AND (c.order_id IS NULL OR c.order_id = 0)"; 
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting active cart for user: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update cart with order, delivery, and payment IDs
    public boolean updateCartWithOrderDetails(int cartId, int orderId, int deliveryId, int paymentId) {
        if (isConnectionError) return false;
        
        String query = "UPDATE cart SET order_id = ?, delivery_id = ?, payment_id = ? WHERE cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, deliveryId);
            pstmt.setInt(3, paymentId);
            pstmt.setInt(4, cartId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating cart with order details: " + e.getMessage());
            return false;
        }
    }
    
    // Update cart totals (count and total price)
    private void updateCartTotals(int cartId) throws SQLException {
        String countQuery = "SELECT COUNT(*) AS item_count FROM cart_details WHERE cart_id = ?";
        String priceQuery = "SELECT SUM(CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                           "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                           "     ELSE m.food_price END) AS total_price " +
                           "FROM cart_details cd " +
                           "JOIN menu m ON cd.food_id = m.food_id " +
                           "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                           "WHERE cd.cart_id = ?";
                           
        int count = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        
        // Get count
        try (PreparedStatement pstmt = dbConn.prepareStatement(countQuery)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("item_count");
                }
            }
        }
        
        // Get total price
        try (PreparedStatement pstmt = dbConn.prepareStatement(priceQuery)) {
            pstmt.setInt(1, cartId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalPrice = rs.getBigDecimal("total_price");
                    if (totalPrice == null) {
                        totalPrice = BigDecimal.ZERO;
                    }
                }
            }
        }
        
        // Update cart
        String updateQuery = "UPDATE cart SET count = ?, total_price = ? WHERE cart_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, count);
            pstmt.setBigDecimal(2, totalPrice);
            pstmt.setInt(3, cartId);
            
            pstmt.executeUpdate();
        }
    }
    
    // Clean up resources
    public void close() {
        if (dbConn != null) {
            try {
                DbConfig.closeConnection(dbConn);
            } catch (Exception e) {
                logger.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }
}