package DAO;

import Database.JDBC;
import Model.FoodItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Dao_FoodItem implements Dao_Interface<FoodItem> {
    public Dao_FoodItem() {
        super();
    }

    @Override
    public List<FoodItem> getAll() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FoodItem item = new FoodItem(
                        rs.getInt("id"),
                        rs.getString("foodName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("categoryId"),
                        rs.getString("imageUrl"),
                        rs.getInt("status"),
                        rs.getString("createdAt")
                );
                foodItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }

    @Override
    public int create(FoodItem foodItem) {
        String sql = "INSERT INTO food_items (foodName, description, price, categoryId, imageUrl, status, createdAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, foodItem.getFoodName());
            stmt.setString(2, foodItem.getDescription());
            stmt.setDouble(3, foodItem.getPrice());
            stmt.setInt(4, foodItem.getCategoryId());
            stmt.setString(5, foodItem.getImageUrl());
            stmt.setInt(6, foodItem.getStatus());
            stmt.setString(7, foodItem.getCreatedAt());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(FoodItem foodItem) {
        String sql = "UPDATE food_items SET foodName = ?, description = ?, price = ?, "
                + "categoryId = ?, imageUrl = ?, status = ?, createdAt = ? WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, foodItem.getFoodName());
            stmt.setString(2, foodItem.getDescription());
            stmt.setDouble(3, foodItem.getPrice());
            stmt.setInt(4, foodItem.getCategoryId());
            stmt.setString(5, foodItem.getImageUrl());
            stmt.setInt(6, foodItem.getStatus());
            stmt.setString(7, foodItem.getCreatedAt());
            stmt.setInt(8, foodItem.getId());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(FoodItem foodItem) {
        String sql = "DELETE FROM food_items WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodItem.getId());
            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public FoodItem selectedById(FoodItem foodItem) {
        String sql = "SELECT * FROM food_items WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodItem.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new FoodItem(
                        rs.getInt("id"),
                        rs.getString("foodName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("categoryId"),
                        rs.getString("imageUrl"),
                        rs.getInt("status"),
                        rs.getString("createdAt")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FoodItem> selectByCondition(String condition) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_items WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FoodItem item = new FoodItem(
                        rs.getInt("id"),
                        rs.getString("foodName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("categoryId"),
                        rs.getString("imageUrl"),
                        rs.getInt("status"),
                        rs.getString("createdAt")
                );
                foodItems.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodItems;
    }
}
