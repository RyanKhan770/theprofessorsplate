package com.TheProfessorsPlate.model;

public class CartDetail {
    private int userId;
    private int foodId;
    private int cartId;
    
    // Default constructor
    public CartDetail() {}
    
    // Parameterized constructor
    public CartDetail(int userId, int foodId, int cartId) {
        this.userId = userId;
        this.foodId = foodId;
        this.cartId = cartId;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getFoodId() {
        return foodId;
    }
    
    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
    
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}