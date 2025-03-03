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
    // method for admin: get all to manage (include active and inactive status)
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
    // dynamic method for admin and client
    @Override
    public List<FoodItem> selectByCondition(String condition) {
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            // "bestseller":
            switch (condition)
            {
                case "bestseller": // admin dashboard method
                    pstmt.setString(1, "order by sold desc limit 3");
                    break;
                case "active": // client method
                    pstmt.setString(1, "where status = true");
            }
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
    // admin method
    public void create(FoodItem foodItem) {
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String query = "insert into fooditems (food_name, description, price, category_id, image_url, stock, sold)" +
                    " values ('"+foodItem.getFoodName()+"', '"+foodItem.getDescription()+"', "+foodItem.getPrice()+", "+foodItem.getCategoryId()+", '"+foodItem.getImageUrl()+"', '"+foodItem.getStock()+"', '"+foodItem.getSold()+"')";
            int result = st.executeUpdate(query);
            System.out.println("You executed: " + query);
            System.out.println("Rows have been changed are: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // admin method
    @Override
    // can not edit sold field => this field auto update base on orer status
    public int update(FoodItem foodItem) {
        int res = 0;
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String query = "update fooditems (food_name, description, price, category_id, image_url, stock)" +
                    "set food_name = '"+foodItem.getFoodName()+"', description = '"+foodItem.getDescription()+"', price = "+foodItem.getPrice()+", category_id: "+foodItem.getCategoryId()+", image_url = '"+foodItem.getImageUrl()+"', stock = "+foodItem.getStock()+"";
            res = st.executeUpdate(query);
            System.out.println("You executed: " + query);
            System.out.println("Rows have been changed are: " + res);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    // admin method
    @Override
    public int delete(FoodItem foodItem) {
        // update status: true => false
        return 0;
    }

    @Override
    public FoodItem selectedById(int foodItemId) {
        FoodItem item = new FoodItem();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from fooditems where id = "+foodItemId+"";
            PreparedStatement pstmt = con.prepareStatement(query);
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
