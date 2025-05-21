package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.CartItem;
import com.TheProfessorsPlate.model.Delivery;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.Payment;

public class OrderService {
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;
    
    public OrderService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
            isConnectionError = false;
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Database connection error: " + ex.getMessage());
            isConnectionError = true;
        }
    }
    
    // Create a new order
    public Order createOrder(int userId, int cartId, String deliveryLocation, String deliveryPhone, 
                           String paymentMethod, BigDecimal totalAmount) {
        if (isConnectionError) return null;
        
        boolean originalAutoCommit = true;
        
        try {
            // Start a transaction
            originalAutoCommit = dbConn.getAutoCommit();
            dbConn.setAutoCommit(false);
            
            // 1. Create payment record
            Payment payment = createPayment(paymentMethod, totalAmount);
            if (payment == null) {
                dbConn.rollback();
                return null;
            }
            
            // 2. Create delivery record
            Delivery delivery = createDelivery(deliveryLocation, deliveryPhone, payment.getPaymentId());
            if (delivery == null) {
                dbConn.rollback();
                return null;
            }
            
            // 3. Get cart details
            Cart cart = getCart(cartId);
            if (cart == null) {
                dbConn.rollback();
                return null;
            }
            
            // 4. Create order
            String orderQuery = "INSERT INTO orders (order_date, order_status, order_quantity, delivery_id, payment_id) " +
                              "VALUES (?, ?, ?, ?, ?)";
            
            Order order = new Order();
            
            try (PreparedStatement pstmt = dbConn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                Timestamp currentTime = new Timestamp(new Date().getTime());
                pstmt.setTimestamp(1, currentTime);
                pstmt.setString(2, "pending"); // Initial status
                pstmt.setInt(3, cart.getCount()); // Use cart count as order quantity
                pstmt.setInt(4, delivery.getDeliveryId());
                pstmt.setInt(5, payment.getPaymentId());
                
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    dbConn.rollback();
                    return null;
                }
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setOrderId(orderId);
                        order.setOrderDate(currentTime);
                        order.setOrderStatus("pending");
                        order.setOrderQuantity(cart.getCount());
                        order.setDeliveryId(delivery.getDeliveryId());
                        order.setPaymentId(payment.getPaymentId());
                        
                        // Also set the full objects
                        order.setDelivery(delivery);
                        order.setPayment(payment);
                    } else {
                        dbConn.rollback();
                        return null;
                    }
                }
            }
            
            // 5. Update cart with the order, delivery, and payment IDs
            String updateCartQuery = "UPDATE cart SET order_id = ?, delivery_id = ?, payment_id = ? WHERE cart_id = ?";
            
            try (PreparedStatement pstmt = dbConn.prepareStatement(updateCartQuery)) {
                pstmt.setInt(1, order.getOrderId());
                pstmt.setInt(2, delivery.getDeliveryId());
                pstmt.setInt(3, payment.getPaymentId());
                pstmt.setInt(4, cartId);
                
                pstmt.executeUpdate();
            }
            
            // If everything went well, commit the transaction
            dbConn.commit();
            
            return order;
            
        } catch (SQLException e) {
            logger.severe("Error creating order: " + e.getMessage());
            try {
                dbConn.rollback();
            } catch (SQLException ex) {
                logger.severe("Error rolling back transaction: " + ex.getMessage());
            }
            return null;
        } finally {
            try {
                dbConn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                logger.severe("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Helper method to create payment
    private Payment createPayment(String paymentMethod, BigDecimal amount) throws SQLException {
        String query = "INSERT INTO payment (payment_date, payment_method, payment_status, payment_amount) " +
                      "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date currentDate = new java.sql.Date(new Date().getTime());
            pstmt.setDate(1, currentDate);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, "pending"); // Initial status
            pstmt.setBigDecimal(4, amount);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int paymentId = generatedKeys.getInt(1);
                    
                    Payment payment = new Payment();
                    payment.setPaymentId(paymentId);
                    payment.setPaymentDate(currentDate);
                    payment.setPaymentMethod(paymentMethod);
                    payment.setPaymentStatus("pending");
                    payment.setPaymentAmount(amount);
                    
                    return payment;
                } else {
                    return null;
                }
            }
        }
    }
    
    // Helper method to create delivery
    private Delivery createDelivery(String location, String phone, int paymentId) throws SQLException {
        String query = "INSERT INTO delivery (delivery_person, delivery_status, delivery_phone, " +
                      "delivery_time, delivery_location, payment_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date deliveryTime = new java.sql.Date(new Date().getTime());
            pstmt.setString(1, "To be assigned"); // Will be assigned later
            pstmt.setString(2, "pending"); // Initial status
            pstmt.setString(3, phone);
            pstmt.setDate(4, deliveryTime);
            pstmt.setString(5, location);
            pstmt.setInt(6, paymentId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int deliveryId = generatedKeys.getInt(1);
                    
                    Delivery delivery = new Delivery();
                    delivery.setDeliveryId(deliveryId);
                    delivery.setDeliveryPerson("To be assigned");
                    delivery.setDeliveryStatus("pending");
                    delivery.setDeliveryPhone(phone);
                    delivery.setDeliveryTime(deliveryTime);
                    delivery.setDeliveryLocation(location);
                    delivery.setPaymentId(paymentId);
                    
                    return delivery;
                } else {
                    return null;
                }
            }
        }
    }
    
    // Helper method to get cart
    private Cart getCart(int cartId) throws SQLException {
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
        }
        
        return null;
    }
    
    // Get order by ID
    public Order getOrderById(int orderId) {
        if (isConnectionError) return null;
        
        String query = "SELECT o.*, p.payment_date, p.payment_method, p.payment_status, p.payment_amount, " +
                      "d.delivery_person, d.delivery_status, d.delivery_phone, d.delivery_time, d.delivery_location, " +
                      "u.user_name " +
                      "FROM orders o " +
                      "JOIN payment p ON o.payment_id = p.payment_id " +
                      "JOIN delivery d ON o.delivery_id = d.delivery_id " +
                      "JOIN cart c ON o.order_id = c.order_id " +
                      "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                      "JOIN user u ON cd.user_id = u.user_id " +
                      "WHERE o.order_id = ? " +
                      "LIMIT 1"; // Get just one row since we only need user info once
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            
            Order order = null;
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setOrderQuantity(rs.getInt("order_quantity"));
                    order.setDeliveryId(rs.getInt("delivery_id"));
                    order.setPaymentId(rs.getInt("payment_id"));
                    
                    // Set customer name
                    order.setCustomerName(rs.getString("user_name"));
                    
                    // Set payment details
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setPaymentDate(rs.getDate("payment_date"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setPaymentStatus(rs.getString("payment_status"));
                    payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
                    order.setPayment(payment);
                    
                    // Set delivery details
                    Delivery delivery = new Delivery();
                    delivery.setDeliveryId(rs.getInt("delivery_id"));
                    delivery.setDeliveryPerson(rs.getString("delivery_person"));
                    delivery.setDeliveryStatus(rs.getString("delivery_status"));
                    delivery.setDeliveryPhone(rs.getString("delivery_phone"));
                    delivery.setDeliveryTime(rs.getDate("delivery_time"));
                    delivery.setDeliveryLocation(rs.getString("delivery_location"));
                    delivery.setPaymentId(rs.getInt("payment_id"));
                    order.setDelivery(delivery);
                    
                    // Get order items (from cart)
                    List<CartItem> items = getOrderItems(orderId);
                    order.setItems(items);
                }
            }
            
            return order;
        } catch (SQLException e) {
            logger.severe("Error getting order by ID: " + e.getMessage());
            return null;
        }
    }
    
    // Get order items
    private List<CartItem> getOrderItems(int orderId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        
        String query = "SELECT cd.*, m.food_name, m.food_description, m.food_price, m.food_image, " +
                      "CASE WHEN d.discount_percentage > 0 THEN m.food_price * (1 - d.discount_percentage/100) " +
                      "     WHEN d.discount_amount > 0 THEN m.food_price - d.discount_amount " +
                      "     ELSE m.food_price END AS discounted_price " +
                      "FROM cart_details cd " +
                      "JOIN cart c ON cd.cart_id = c.cart_id " +
                      "JOIN menu m ON cd.food_id = m.food_id " +
                      "LEFT JOIN discount d ON m.discount_id = d.discount_id " +
                      "WHERE c.order_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            
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
        }
        
        return items;
    }
    
    // Get orders by user ID
    public List<Order> getOrdersByUserId(int userId) {
        if (isConnectionError) return new ArrayList<>();
        
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();
        
        String query = "SELECT o.*, p.payment_date, p.payment_method, p.payment_status, p.payment_amount, " +
                      "d.delivery_person, d.delivery_status, d.delivery_phone, d.delivery_time, d.delivery_location " +
                      "FROM orders o " +
                      "JOIN payment p ON o.payment_id = p.payment_id " +
                      "JOIN delivery d ON o.delivery_id = d.delivery_id " +
                      "JOIN cart c ON o.order_id = c.order_id " +
                      "JOIN cart_details cd ON c.cart_id = cd.cart_id " +
                      "WHERE cd.user_id = ? " +
                      "GROUP BY o.order_id " +
                      "ORDER BY o.order_date DESC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    
                    if (!orderMap.containsKey(orderId)) {
                        Order order = new Order();
                        order.setOrderId(orderId);
                        order.setOrderDate(rs.getTimestamp("order_date"));
                        order.setOrderStatus(rs.getString("order_status"));
                        order.setOrderQuantity(rs.getInt("order_quantity"));
                        order.setDeliveryId(rs.getInt("delivery_id"));
                        order.setPaymentId(rs.getInt("payment_id"));
                        
                        // Set payment details
                        Payment payment = new Payment();
                        payment.setPaymentId(rs.getInt("payment_id"));
                        payment.setPaymentDate(rs.getDate("payment_date"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setPaymentStatus(rs.getString("payment_status"));
                        payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
                        order.setPayment(payment);
                        
                        // Set delivery details
                        Delivery delivery = new Delivery();
                        delivery.setDeliveryId(rs.getInt("delivery_id"));
                        delivery.setDeliveryPerson(rs.getString("delivery_person"));
                        delivery.setDeliveryStatus(rs.getString("delivery_status"));
                        delivery.setDeliveryPhone(rs.getString("delivery_phone"));
                        delivery.setDeliveryTime(rs.getDate("delivery_time"));
                        delivery.setDeliveryLocation(rs.getString("delivery_location"));
                        delivery.setPaymentId(rs.getInt("payment_id"));
                        order.setDelivery(delivery);
                        
                        orderMap.put(orderId, order);
                    }
                }
            }
            
            // Now get items for each order
            for (Map.Entry<Integer, Order> entry : orderMap.entrySet()) {
                int orderId = entry.getKey();
                Order order = entry.getValue();
                
                order.setItems(getOrderItems(orderId));
                orders.add(order);
            }
            
            return orders;
        } catch (SQLException e) {
            logger.severe("Error getting orders by user ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        if (isConnectionError) return false;
        
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