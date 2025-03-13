package DAO;

import Database.JDBC;
import Model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Food implements Dao_Interface<FoodItem> {

    public static Dao_Food getInstance()
    {
        return new Dao_Food();
    }
    @Override
    public List<FoodItem> getAll() {
        List<FoodItem> foodItems = new ArrayList<>();
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
                int sold = rs.getInt("sold");
                FoodItem item = new FoodItem(id, foodName, desc, price, stock, categoryId, imageURL, status, createdAt, sold);
                foodItems.add(item);
            }
            JDBC.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return foodItems;
    }
    @Override
    public List<FoodItem> selectByCondition(String condition) {
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems ";
            switch (condition)
            {
                case "bestseller": // admin dashboard method
                    query += "order by sold desc limit 2";
                    break;
                case "active": // client method
                    query += "where status = true";
                    break;
                default:
                    break;
            }
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
                int sold = rs.getInt("sold");
                FoodItem item = new FoodItem(id, foodName, desc, price, stock, categoryId, imageURL, status, createdAt, sold);
                foodItems.add(item);
            }
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return foodItems;
    }
    @Override
    public void create(FoodItem foodItem) {
        try {
            Connection con = JDBC.getConnection();
            String query = "insert into fooditems (food_name, description, price, category_id, image_url, stock)" +
                    " values (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, foodItem.getFoodName());
            pstmt.setString(2, foodItem.getDescription());
            pstmt.setDouble(3, foodItem.getPrice());
            pstmt.setInt(4, foodItem.getCategoryId());
            pstmt.setString(5, foodItem.getImageUrl());
            pstmt.setInt(6, foodItem.getStock());
            int result = pstmt.executeUpdate();
            System.out.println("You executed: " + query);
            System.out.println("Rows have been changed are: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    // id readonly
    public int update(FoodItem foodItem) {
        int res = 0;
        try {
            Connection con = JDBC.getConnection();
            String query = "update fooditems " +
                    "set food_name = ?, description = ?, price = ?, category_id: ?, image_url = ?, stock = ? where id = ?";
            // where condition ?
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, foodItem.getFoodName());
            pstmt.setString(2, foodItem.getDescription());
            pstmt.setDouble(3, foodItem.getPrice());
            pstmt.setInt(4, foodItem.getCategoryId());
            pstmt.setString (5, foodItem.getImageUrl());
            pstmt.setInt(6, foodItem.getStock());
            pstmt.setInt(7, foodItem.getId());
            res = pstmt.executeUpdate();
            System.out.println("You executed: " + query);
            System.out.println("Rows have been changed are: " + res);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    @Override
    public int delete(FoodItem foodItem) {
        int res = 0;
        try {
            Connection con = JDBC.getConnection();
            String query = "update fooditems " +
                    "set status = false where id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, foodItem.getId());
            res = pstmt.executeUpdate();
            System.out.println("You executed: " + query);
            System.out.println("Rows have been changed are: " + res);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public FoodItem selectedById(int foodItemId) {
        FoodItem item = new FoodItem();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems where id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, foodItemId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                item.setId(rs.getInt("id"));
                item.setFoodName(rs.getString("food_name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setImageUrl(rs.getString("image_url"));
                item.setStatus(rs.getBoolean("status"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
                item.setStock(rs.getInt("stock"));
                item.setSold(rs.getInt("sold"));
            }
            JDBC.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }
}