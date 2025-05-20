package com.TheProfessorsPlate.model;

import java.util.Date;

public class Delivery {
    private int deliveryId;
    private String deliveryPerson;
    private String deliveryStatus;
    private String deliveryPhone;
    private Date deliveryTime;
    private String deliveryLocation;
    private int paymentId;
    
    // Default constructor
    public Delivery() {}
    
    // Parameterized constructor
    public Delivery(int deliveryId, String deliveryPerson, String deliveryStatus, String deliveryPhone,
                   Date deliveryTime, String deliveryLocation, int paymentId) {
        this.deliveryId = deliveryId;
        this.deliveryPerson = deliveryPerson;
        this.deliveryStatus = deliveryStatus;
        this.deliveryPhone = deliveryPhone;
        this.deliveryTime = deliveryTime;
        this.deliveryLocation = deliveryLocation;
        this.paymentId = paymentId;
    }
    
    // Constructor without ID (for new delivery creation)
    public Delivery(String deliveryPerson, String deliveryStatus, String deliveryPhone,
                   Date deliveryTime, String deliveryLocation, int paymentId) {
        this.deliveryPerson = deliveryPerson;
        this.deliveryStatus = deliveryStatus;
        this.deliveryPhone = deliveryPhone;
        this.deliveryTime = deliveryTime;
        this.deliveryLocation = deliveryLocation;
        this.paymentId = paymentId;
    }
    
    // Getters and Setters
    public int getDeliveryId() {
        return deliveryId;
    }
    
    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }
    
    public String getDeliveryPerson() {
        return deliveryPerson;
    }
    
    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }
    
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    
    public String getDeliveryPhone() {
        return deliveryPhone;
    }
    
    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }
    
    public Date getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getDeliveryLocation() {
        return deliveryLocation;
    }
    
    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
    
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}