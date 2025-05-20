package com.TheProfessorsPlate.model;

public class Cart {
    private int cartId;
    private int count;
    private double totalPrice;
    private int orderId;
    private int deliveryId;
    private int paymentId;
    
    // Default constructor
    public Cart() {}
    
    // Parameterized constructor
    public Cart(int cartId, int count, double totalPrice, int orderId, int deliveryId, int paymentId) {
        this.cartId = cartId;
        this.count = count;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }
    
    // Constructor without ID (for new cart creation)
    public Cart(int count, double totalPrice, int orderId, int deliveryId, int paymentId) {
        this.count = count;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }
    
    // Getters and Setters
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
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