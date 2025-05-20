package com.TheProfessorsPlate.model;

import java.util.Date;

public class Order {
    private int orderId;
    private Date orderDate;
    private String orderStatus;
    private int orderQuantity;
    private int deliveryId;
    private int paymentId;
    
    // Default constructor
    public Order() {}
    
    // Parameterized constructor
    public Order(int orderId, Date orderDate, String orderStatus, int orderQuantity, 
                int deliveryId, int paymentId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderQuantity = orderQuantity;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }
    
    // Constructor without ID (for new order creation)
    public Order(Date orderDate, String orderStatus, int orderQuantity, 
                int deliveryId, int paymentId) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderQuantity = orderQuantity;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
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
}