package com.TheProfessorsPlate.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private Timestamp orderDate;
    private String orderStatus;
    private int orderQuantity;
    private int deliveryId;
    private int paymentId;
    
    // Original additional fields
    private Delivery delivery;
    private Payment payment;
    private List<CartItem> items;
    private String customerName;
    
    // Enhanced fields for admin dashboard
    private BigDecimal orderAmount;
    private String paymentMethod;
    private String paymentStatus;
    private int totalItems;
    private User customer;
    private List<OrderItem> orderItems; // For detailed order view in admin panel
    
    public Order() {
    }
    
    public Order(int orderId, Timestamp orderDate, String orderStatus, int orderQuantity, int deliveryId, int paymentId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderQuantity = orderQuantity;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }

    // Original getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    // Enhanced getters and setters for admin dashboard
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
    
    // Overloaded method for double values
    public void setOrderAmount(double amount) {
        this.orderAmount = new BigDecimal(String.valueOf(amount));
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    /**
     * Helper method to calculate the total amount of the order from either items or orderAmount
     * @return The total amount of the order as a BigDecimal
     */
    public BigDecimal calculateTotal() {
        if (orderAmount != null && orderAmount.compareTo(BigDecimal.ZERO) > 0) {
            return orderAmount;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        // Try to calculate from CartItem list
        if (items != null && !items.isEmpty()) {
            for (CartItem item : items) {
                BigDecimal itemPrice = item.getPrice();
                BigDecimal quantity = new BigDecimal(item.getQuantity());
                total = total.add(itemPrice.multiply(quantity));
            }
            return total;
        }
        
        // Try to calculate from OrderItem list
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem item : orderItems) {
                BigDecimal itemPrice = new BigDecimal(String.valueOf(item.getPrice()));
                BigDecimal quantity = new BigDecimal(item.getQuantity());
                total = total.add(itemPrice.multiply(quantity));
            }
            return total;
        }
        
        // If payment is available, use payment amount
        if (payment != null && payment.getPaymentAmount() != null) {
            return payment.getPaymentAmount();
        }
        
        return BigDecimal.ZERO;
    }

    /**
     * Get order status color class for UI display
     * @return CSS class name for the status
     */
    public String getStatusClass() {
        if (orderStatus == null) return "";
        
        switch(orderStatus.toLowerCase()) {
            case "pending": return "pending";
            case "processing": return "processing";
            case "shipping": return "shipping";
            case "delivered": return "delivered";
            case "cancelled": return "cancelled";
            default: return "";
        }
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
                + ", orderQuantity=" + orderQuantity + ", customerName=" + customerName + "]";
    }
}