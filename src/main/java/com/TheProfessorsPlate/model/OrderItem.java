package com.TheProfessorsPlate.model;

import java.math.BigDecimal;

public class OrderItem {
    private int foodId;
    private String foodName;
    private String foodImage;
    private String foodCategory;
    private int quantity;
    private BigDecimal price;
    
    public OrderItem() {
    }
    
    public OrderItem(int foodId, String foodName, String foodImage, String foodCategory, int quantity, BigDecimal price) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodCategory = foodCategory;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Constructor that accepts double for price
    public OrderItem(int foodId, String foodName, String foodImage, String foodCategory, int quantity, double price) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodCategory = foodCategory;
        this.quantity = quantity;
        this.price = new BigDecimal(String.valueOf(price));
    }
    
    public int getFoodId() {
        return foodId;
    }
    
    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public String getFoodImage() {
        return foodImage;
    }
    
    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
    
    public String getFoodCategory() {
        return foodCategory;
    }
    
    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    // Overloaded setter for double values
    public void setPrice(double price) {
        this.price = new BigDecimal(String.valueOf(price));
    }
    
    public BigDecimal getSubtotal() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(new BigDecimal(quantity));
    }
    
    // Overloaded method for compatibility
    public double getPriceAsDouble() {
        return price != null ? price.doubleValue() : 0.0;
    }
}