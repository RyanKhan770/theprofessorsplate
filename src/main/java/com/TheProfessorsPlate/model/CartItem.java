package com.TheProfessorsPlate.model;

import java.math.BigDecimal;

public class CartItem {
    private int cartDetailId;
    private int userId;
    private int foodId;
    private int cartId;
    private int quantity;
    
    // Additional fields for convenience (not stored in cart_details table)
    private String foodName;
    private String foodDescription;
    private String foodImage;
    private BigDecimal foodPrice;
    private BigDecimal discountedPrice;
    
    public CartItem() {
    }
    
    public CartItem(int userId, int foodId, int cartId, int quantity) {
        this.userId = userId;
        this.foodId = foodId;
        this.cartId = cartId;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

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
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public BigDecimal getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(BigDecimal foodPrice) {
        this.foodPrice = foodPrice;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @Override
    public String toString() {
        return "CartItem [cartDetailId=" + cartDetailId + ", userId=" + userId + ", foodId=" + foodId + ", cartId="
                + cartId + ", quantity=" + quantity + ", foodName=" + foodName + ", foodPrice=" + foodPrice + "]";
    }
}