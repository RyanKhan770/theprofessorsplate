package com.TheProfessorsPlate.model;

import java.util.Date;

public class Discount {
    private int discountId;
    private String discountName;
    private double discountPercentage;
    private double discountAmount;
    private Date discountPeriod;
    
    // Default constructor
    public Discount() {}
    
    // Parameterized constructor
    public Discount(int discountId, String discountName, double discountPercentage, 
                   double discountAmount, Date discountPeriod) {
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.discountPeriod = discountPeriod;
    }
    
    // Constructor without ID (for new discount creation)
    public Discount(String discountName, double discountPercentage, 
                   double discountAmount, Date discountPeriod) {
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.discountPeriod = discountPeriod;
    }
    
    // Getters and Setters
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
}