package com.TheProfessorsPlate.model;

import java.util.Date;

public class Discount {
    private int discountId;
    private String discountName;
    private double discountPercentage;
    private double discountAmount;
    private Date discountPeriod;  // This might be null
    
    // Default constructor
    public Discount() {
    }
    
    // Constructor with all fields
    public Discount(int discountId, String discountName, double discountPercentage, 
                  double discountAmount, Date discountPeriod) {
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.discountPeriod = discountPeriod;
    }
    
    // Getters and setters
    public int getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
    
    public String getDiscountName() {
        return discountName;
    }
    
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public Date getDiscountPeriod() {
        return discountPeriod;
    }
    
    public void setDiscountPeriod(Date discountPeriod) {
        this.discountPeriod = discountPeriod;
    }
    
    // Helper method to determine if discount is valid (not expired)
    public boolean isValid() {
        if (discountPeriod == null) {
            // If no expiration date is set, the discount is always valid
            return true;
        }
        // Check if current date is before or equal to expiration date
        return new Date().compareTo(discountPeriod) <= 0;
    }
    
    // Helper method to get the effective discount value for a given price
    public double calculateDiscountValue(double originalPrice) {
        if (discountPercentage > 0) {
            return originalPrice * (discountPercentage / 100.0);
        } else if (discountAmount > 0) {
            return Math.min(discountAmount, originalPrice);  // Don't discount more than the price
        }
        return 0.0;
    }
    
    // Helper method to get the final price after applying discount
    public double calculateFinalPrice(double originalPrice) {
        if (!isValid()) {
            return originalPrice;  // No discount if expired
        }
        
        if (discountPercentage > 0) {
            return originalPrice * (1 - (discountPercentage / 100.0));
        } else if (discountAmount > 0) {
            return Math.max(0, originalPrice - discountAmount);  // Don't go below zero
        }
        return originalPrice;
    }
    
    @Override
    public String toString() {
        return "Discount [discountId=" + discountId +
               ", discountName=" + discountName + 
               ", discountPercentage=" + discountPercentage + 
               ", discountAmount=" + discountAmount + 
               ", discountPeriod=" + (discountPeriod != null ? discountPeriod : "No Expiration") + "]";
    }
}