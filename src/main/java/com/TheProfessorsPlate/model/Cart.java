package com.TheProfessorsPlate.model;

import java.math.BigDecimal;

public class Cart {
    private int cartId;
    private int count;
    private BigDecimal totalPrice;
    private int orderId;
    private int deliveryId;
    private int paymentId;
    
    // Default constructor
    public Cart() {
    }
    
    // Parameterized constructor
    public Cart(int cartId, int count, BigDecimal totalPrice, int orderId, int deliveryId, int paymentId) {
        this.cartId = cartId;
        this.count = count;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.paymentId = paymentId;
    }

    // Getters and setters
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
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

    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", count=" + count + ", totalPrice=" + totalPrice + ", orderId=" + orderId
                + ", deliveryId=" + deliveryId + ", paymentId=" + paymentId + "]";
    }
}