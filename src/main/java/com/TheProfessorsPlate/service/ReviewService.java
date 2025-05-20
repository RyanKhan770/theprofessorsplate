package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM review";

        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Review review = new Review(
                        resultSet.getInt("review_id"),
                        resultSet.getDouble("rating"),
                        resultSet.getString("review_description"),
                        resultSet.getString("review_date")
                );
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}