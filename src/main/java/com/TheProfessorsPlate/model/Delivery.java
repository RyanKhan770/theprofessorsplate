package com.TheProfessorsPlate.model;

import java.util.Date;

public class Delivery {
    // Fields from the original Delivery class
    private int deliveryId;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String deliveryMethod;
    private String specialInstructions;
    
    // Additional fields for DeliveryService
    private String deliveryPerson;
    private String deliveryStatus;
    private String deliveryPhone;
    private Date deliveryTime;
    private String deliveryLocation;
    private int paymentId;
    
    // Default constructor
    public Delivery() {
    }
    
    // Constructor for original fields
    public Delivery(int deliveryId, String address, String city, String state, String zipCode, 
                   String deliveryMethod, String specialInstructions) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.deliveryMethod = deliveryMethod;
        this.specialInstructions = specialInstructions;
    }
    
    // Constructor for DeliveryService fields
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

    // Getters and setters for original fields
    public int getDeliveryId() {
        return deliveryId;
    }
    
    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getDeliveryMethod() {
        return deliveryMethod;
    }
    
    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    // Getters and setters for DeliveryService fields
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
    
    // Helper method to get formatted full address
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null) sb.append(address);
        
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        
        if (state != null && !state.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(state);
        }
        
        if (zipCode != null && !zipCode.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(zipCode);
        }
        
        return sb.toString();
    }
    
    // Helper method to check if delivery is active
    public boolean isActive() {
        return deliveryStatus != null && 
               (deliveryStatus.equalsIgnoreCase("processing") || 
                deliveryStatus.equalsIgnoreCase("shipping") || 
                deliveryStatus.equalsIgnoreCase("in transit"));
    }
    
    @Override
    public String toString() {
        return "Delivery [deliveryId=" + deliveryId + 
               ", address=" + address + 
               ", city=" + city + 
               ", state=" + state + 
               ", zipCode=" + zipCode + 
               ", deliveryMethod=" + deliveryMethod + "]";
    }
}