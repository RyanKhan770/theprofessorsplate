package com.TheProfessorsPlate.model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private Timestamp orderDate;
    private String orderStatus;
    private int orderQuantity;
    private int deliveryId;
    private int paymentId;
    
    // Additional fields for convenience
    private Delivery delivery;
    private Payment payment;
    private List<CartItem> items;
    private String customerName;
    
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

    // Getters and setters
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

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
                + ", orderQuantity=" + orderQuantity + ", deliveryId=" + deliveryId + ", paymentId=" + paymentId + "]";
    }
}