package DAO;

import Database.JDBC;
import Model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Food implements Dao_Interface<FoodItem> {

    @Override
    // method for admin: get all to manage
    public List<FoodItem> getAll() {
        List<FoodItem> foodItems = new ArrayList<>();
        // interact with database to get all food items
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String foodName = rs.getString("food_name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int categoryId = rs.getInt("category_id");
                String imageURL = rs.getString("image_url");
                boolean status = rs.getBoolean("status");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int stock = rs.getInt("stock");
                FoodItem item = new FoodItem(id, foodName, desc, price, stock, categoryId, imageURL, status, createdAt);
                foodItems.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    @Override
    // admin method
    public int create(FoodItem foodItem) {
        return 0;
    }
    // admin method
    @Override
    public int update(FoodItem foodItem) {
        return 0;
    }
    // admin method
    @Override
    public int delete(FoodItem foodItem) {
        return 0;
    }

    @Override
    public FoodItem selectedById(FoodItem foodItem) {
        return null;
    }

    @Override
    public List<FoodItem> selectByCondition(String condition) {
        return List.of();
    }
    // client method
    public List<FoodItem> selectActiveFood()
    {
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems where status = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, true);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String foodName = rs.getString("food_name");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int categoryId = rs.getInt("category_id");
                String imageURL = rs.getString("image_url");
                boolean status = rs.getBoolean("status");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int stock = rs.getInt("stock");
                FoodItem item = new FoodItem(id, foodName, desc, price, stock, categoryId, imageURL, status, createdAt);
                foodItems.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }
}
