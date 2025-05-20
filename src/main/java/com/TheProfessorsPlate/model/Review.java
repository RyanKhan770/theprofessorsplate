package com.TheProfessorsPlate.model;

public class Review {
    private int reviewId;
    private double rating;
    private String reviewDescription;
    private String reviewDate;
    
    // Default constructor
    public Review() {}
    
    // Parameterized constructor
    public Review(int reviewId, double rating, String reviewDescription, String reviewDate) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.reviewDescription = reviewDescription;
        this.reviewDate = reviewDate;
    }
    
    // Constructor without ID (for new review creation)
    public Review(double rating, String reviewDescription, String reviewDate) {
        this.rating = rating;
        this.reviewDescription = reviewDescription;
        this.reviewDate = reviewDate;
    }
    
    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getReviewDescription() {
        return reviewDescription;
    }
    
    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }
    
    public String getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}