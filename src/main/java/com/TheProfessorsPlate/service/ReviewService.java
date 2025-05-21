package com.TheProfessorsPlate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Review;

public class ReviewService {
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());
    private Connection dbConn;
    private boolean isConnectionError;

    public ReviewService() {
        try {
            dbConn = DbConfig.getDbConnection();
            logger.info("Database connection established successfully for ReviewService");
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to establish database connection: " + ex.getMessage());
            isConnectionError = true;
        }
    }
    
    // Create a new review
    public Integer addReview(int userId, BigDecimal rating, String description) {
        if (isConnectionError) return null;

        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = dbConn.getAutoCommit();
            dbConn.setAutoCommit(false);
            
            // Create review record
            String reviewQuery = "INSERT INTO review (rating, review_date, review_description) VALUES (?, ?, ?)";
            int reviewId;
            
            try (PreparedStatement pstmt = dbConn.prepareStatement(reviewQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setBigDecimal(1, rating);
                pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
                pstmt.setString(3, description);
                
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    dbConn.rollback();
                    logger.warning("Creating review failed, no rows affected.");
                    return null;
                }
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reviewId = generatedKeys.getInt(1);
                    } else {
                        dbConn.rollback();
                        logger.warning("Creating review failed, no ID obtained.");
                        return null;
                    }
                }
            }
            
            // Create review_details record
            String detailsQuery = "INSERT INTO review_details (user_id, review_id) VALUES (?, ?)";
            
            try (PreparedStatement pstmt = dbConn.prepareStatement(detailsQuery)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, reviewId);
                
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    dbConn.rollback();
                    logger.warning("Creating review details failed, no rows affected.");
                    return null;
                }
            }
            
            // Commit transaction
            dbConn.commit();
            logger.info("Review added successfully with ID: " + reviewId);
            return reviewId;
            
        } catch (SQLException e) {
            try {
                dbConn.rollback();
            } catch (SQLException ex) {
                logger.severe("Error rolling back transaction: " + ex.getMessage());
            }
            logger.severe("Error adding review: " + e.getMessage());
            return null;
        } finally {
            try {
                dbConn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                logger.severe("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Get all reviews with user information
    public List<Review> getAllReviews() {
        if (isConnectionError) return new ArrayList<>();

        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.user_name, u.user_image " +
                       "FROM review r " +
                       "JOIN review_details rd ON r.review_id = rd.review_id " +
                       "JOIN user u ON rd.user_id = u.user_id " +
                       "ORDER BY r.review_date DESC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setRating(rs.getBigDecimal("rating"));
                review.setReviewDate(rs.getTimestamp("review_date"));
                review.setReviewDescription(rs.getString("review_description"));
                review.setUserName(rs.getString("user_name"));
                review.setUserImage(rs.getString("user_image"));
                
                reviews.add(review);
            }
            
            logger.info("Retrieved " + reviews.size() + " reviews");
        } catch (SQLException e) {
            logger.severe("Error retrieving reviews: " + e.getMessage());
        }
        
        return reviews;
    }
    
    // Get reviews by user ID
    public List<Review> getReviewsByUserId(int userId) {
        if (isConnectionError) return new ArrayList<>();

        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.user_name, u.user_image " +
                       "FROM review r " +
                       "JOIN review_details rd ON r.review_id = rd.review_id " +
                       "JOIN user u ON rd.user_id = u.user_id " +
                       "WHERE rd.user_id = ? " +
                       "ORDER BY r.review_date DESC";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setRating(rs.getBigDecimal("rating"));
                    review.setReviewDate(rs.getTimestamp("review_date"));
                    review.setReviewDescription(rs.getString("review_description"));
                    review.setUserName(rs.getString("user_name"));
                    review.setUserImage(rs.getString("user_image"));
                    
                    reviews.add(review);
                }
            }
            
            logger.info("Retrieved " + reviews.size() + " reviews for user ID: " + userId);
        } catch (SQLException e) {
            logger.severe("Error retrieving reviews by user ID: " + e.getMessage());
        }
        
        return reviews;
    }
    
    // Get review by ID
    public Review getReviewById(int reviewId) {
        if (isConnectionError) return null;

        String query = "SELECT r.*, u.user_name, u.user_image " +
                       "FROM review r " +
                       "JOIN review_details rd ON r.review_id = rd.review_id " +
                       "JOIN user u ON rd.user_id = u.user_id " +
                       "WHERE r.review_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setInt(1, reviewId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setRating(rs.getBigDecimal("rating"));
                    review.setReviewDate(rs.getTimestamp("review_date"));
                    review.setReviewDescription(rs.getString("review_description"));
                    review.setUserName(rs.getString("user_name"));
                    review.setUserImage(rs.getString("user_image"));
                    
                    logger.info("Retrieved review with ID: " + reviewId);
                    return review;
                }
            }
            
            logger.warning("Review with ID: " + reviewId + " not found");
        } catch (SQLException e) {
            logger.severe("Error retrieving review by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update review
    public boolean updateReview(int reviewId, BigDecimal rating, String description) {
        if (isConnectionError) return false;

        String query = "UPDATE review SET rating = ?, review_description = ? WHERE review_id = ?";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
            pstmt.setBigDecimal(1, rating);
            pstmt.setString(2, description);
            pstmt.setInt(3, reviewId);
            
            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                logger.info("Review with ID: " + reviewId + " updated successfully");
            } else {
                logger.warning("Review with ID: " + reviewId + " not found for update");
            }
            
            return success;
        } catch (SQLException e) {
            logger.severe("Error updating review: " + e.getMessage());
            return false;
        }
    }
    
    // Delete review
    public boolean deleteReview(int reviewId) {
        if (isConnectionError) return false;

        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = dbConn.getAutoCommit();
            dbConn.setAutoCommit(false);
            
            // Delete review details first (foreign key constraint)
            String detailsQuery = "DELETE FROM review_details WHERE review_id = ?";
            try (PreparedStatement pstmt = dbConn.prepareStatement(detailsQuery)) {
                pstmt.setInt(1, reviewId);
                pstmt.executeUpdate();
            }
            
            // Delete review
            String reviewQuery = "DELETE FROM review WHERE review_id = ?";
            try (PreparedStatement pstmt = dbConn.prepareStatement(reviewQuery)) {
                pstmt.setInt(1, reviewId);
                
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    dbConn.rollback();
                    logger.warning("Review with ID: " + reviewId + " not found for deletion");
                    return false;
                }
            }
            
            // Commit transaction
            dbConn.commit();
            logger.info("Review with ID: " + reviewId + " deleted successfully");
            return true;
            
        } catch (SQLException e) {
            try {
                dbConn.rollback();
            } catch (SQLException ex) {
                logger.severe("Error rolling back transaction: " + ex.getMessage());
            }
            logger.severe("Error deleting review: " + e.getMessage());
            return false;
        } finally {
            try {
                dbConn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                logger.severe("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Get average rating
    public BigDecimal getAverageRating() {
        if (isConnectionError) return new BigDecimal(0);

        String query = "SELECT AVG(rating) as avg_rating FROM review";
        
        try (PreparedStatement pstmt = dbConn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                BigDecimal avgRating = rs.getBigDecimal("avg_rating");
                logger.info("Retrieved average rating: " + avgRating);
                return avgRating != null ? avgRating : new BigDecimal(0);
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving average rating: " + e.getMessage());
        }
        
        return new BigDecimal(0);
    }
}