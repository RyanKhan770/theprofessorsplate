package com.TheProfessorsPlate.service;

import com.TheProfessorsPlate.config.DbConfig;
import com.TheProfessorsPlate.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    public List<Menu> getAllMenuItems() {
        List<Menu> menuList = new ArrayList<>();
        String query = "SELECT * FROM menu";

        try (Connection connection = DbConfig.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Menu menu = new Menu(
                        resultSet.getInt("food_id"),
                        resultSet.getString("food_name"),
                        resultSet.getString("food_description"),
                        resultSet.getDouble("food_price"),
                        resultSet.getString("food_category"),
                        resultSet.getString("food_image"),
                        resultSet.getInt("discount_id")
                );
                menuList.add(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuList;
    }
}