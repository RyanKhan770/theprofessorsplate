package com.TheProfessorsPlate.model;

import java.math.BigDecimal;

public class CartItem {
    private int cartDetailId;
    private int cartId;
    private int foodId;
    private int quantity;
    private BigDecimal price;
    private int userId;
    
    // Additional fields for convenience
    private String foodName;
    private String foodDescription;
    private BigDecimal foodPrice;
    private String foodImage;
    private BigDecimal discountedPrice;
    private Menu menuItem;
    
    public CartItem() {
    }
    
    public CartItem(int cartDetailId, int cartId, int foodId, int quantity, BigDecimal price, int userId) {
        this.cartDetailId = cartDetailId;
        this.cartId = cartId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
    }

    // Original getters and setters
    public int getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    // Additional getters and setters
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

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    
    public Menu getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Menu menuItem) {
        this.menuItem = menuItem;
    }
    
    // Calculate subtotal
    public BigDecimal getSubtotal() {
        // Use discountedPrice if available, otherwise use price
        BigDecimal priceToUse = discountedPrice != null ? discountedPrice : 
                               (price != null ? price : BigDecimal.ZERO);
        return priceToUse.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "CartItem [cartDetailId=" + cartDetailId + ", cartId=" + cartId + ", foodId=" + foodId + 
               ", quantity=" + quantity + ", foodName=" + foodName + "]";
    }
}