package DAO;

import Config.JDBC;
import Model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Category implements Dao_Interface<Category> {

    public static Dao_Category getInstance()
    {
        return new Dao_Category();
    }
    @Override
    public List<Category> getAll() {
        List<Category> categoryItems = new ArrayList<>();
        String query = "select * from categories";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String categoryName = rs.getString("category_name");
                String description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");
                boolean status = rs.getBoolean("status");
                Category item = new Category(id, categoryName, description, createdAt, status);
                categoryItems.add(item);
            }
            JDBC.closeConnection(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categoryItems;
    }

    @Override
    public void create(Category category) {
        String query = "insert into categories (category_name, description)" +
                "values (?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            int res = pstmt.executeUpdate(); // return rows have been changed;
            System.out.println("You have created new category!");
            System.out.println("Rows have been changed: " + res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int update(Category category) {
        String query = "update categories set category_name = ?, description = ?, status = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setBoolean(3, category.getStatus());
            pstmt.setInt(4, category.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int delete(Category category) {
        String query = "delete from categories where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, category.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public Category selectedById(int id) {
        Category item = new Category();
        String query = "select * from category where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                item.setId(rs.getInt("id"));
                item.setCategoryName(rs.getString("category_name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(rs.getBoolean("status"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    @Override
    public List<Category> selectByCondition(String condition) {
        List<Category> categoryItems = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from categories";
            switch (condition)
            {
                case "categoriesBtn":
//                    query += "order by sold desc limit 2";
                    break;
                default:
                    break;
            }
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String categoryName = rs.getString("category_name");
                String desc = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");
                boolean status = rs.getBoolean("status");
                Category item = new Category(id, categoryName, desc, createdAt, status);
                categoryItems.add(item);
            }
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryItems;
    }
    public boolean checkCategoryActive(int id) {
        String query = "select status from categories where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("status");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
