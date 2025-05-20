package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class OrderService {
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());
    private Connection dbConn;
    
    public OrderService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
        }
    }
    
    // Create a new order
    public Order createOrder(Order order) {
        if (dbConn == null) {
            logger.severe("Database connection is not available.");
            return null;
        }
        
        String query = "INSERT INTO orders (order_date, order_status, order_quantity, delivery_id, payment_id) " +
                       "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, new java.sql.Timestamp(order.getOrderDate().getTime()));
            pstmt.setString(2, order.getOrderStatus());
            pstmt.setInt(3, order.getOrderQuantity());
            pstmt.setInt(4, order.getDeliveryId());
            pstmt.setInt(5, order.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            
            return order;
        } catch (SQLException e) {
            logger.severe("Error creating order: " + e.getMessage());
            return null;
        }
    }
    
    // Get order by ID
    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getString("order_status"),
                        rs.getInt("order_quantity"),
                        rs.getInt("delivery_id"),
                        rs.getInt("payment_id")
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting order by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
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
        } catch (SQLException e) {
            logger.severe("Error getting all orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    // Get orders by user ID (through cart_details)
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT DISTINCT o.* FROM orders o " +
                       "JOIN cart c ON o.order_id = c.order_id " +
                       "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                       "WHERE cd.user_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
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
            logger.severe("Error getting orders by user ID: " + e.getMessage());
        }
        
        return orders;
    }
    
    // Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    // Update entire order
    public boolean updateOrder(Order order) {
        String query = "UPDATE orders SET order_date = ?, order_status = ?, order_quantity = ?, " +
                       "delivery_id = ?, payment_id = ? WHERE order_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setTimestamp(1, new java.sql.Timestamp(order.getOrderDate().getTime()));
            pstmt.setString(2, order.getOrderStatus());
            pstmt.setInt(3, order.getOrderQuantity());
            pstmt.setInt(4, order.getDeliveryId());
            pstmt.setInt(5, order.getPaymentId());
            pstmt.setInt(6, order.getOrderId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating order: " + e.getMessage());
            return false;
        }
    }
    
    // Delete order
    public boolean deleteOrder(int orderId) {
        String query = "DELETE FROM orders WHERE order_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting order: " + e.getMessage());
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