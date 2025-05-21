package com.TheProfessorsPlate.model;

import java.math.BigDecimal;

public class Menu {
    private int foodId;
    private String foodName;
    private String foodDescription;
    private BigDecimal foodPrice;
    private String foodCategory;
    private String foodImage;
    private int discountId;
    
    // Calculated field
    private BigDecimal discountedPrice;
    
    // Constructors
    public Menu() {
    }
    
    public Menu(int foodId, String foodName, String foodDescription, BigDecimal foodPrice, 
                String foodCategory, String foodImage, int discountId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodCategory = foodCategory;
        this.foodImage = foodImage;
        this.discountId = discountId;
    }
    
    // Getters and setters
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
    
    public String getFoodDescription() {
        return foodDescription;
    }
    
    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }
    
    public BigDecimal getFoodPrice() {
        return foodPrice;
    }
    
    public void setFoodPrice(BigDecimal foodPrice) {
        this.foodPrice = foodPrice;
    }
    
    public String getFoodCategory() {
        return foodCategory;
    }
    
    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
    
    public String getFoodImage() {
        return foodImage;
    }
    
    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
    
    public int getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
    
    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }
    
    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}