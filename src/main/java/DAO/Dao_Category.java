package DAO;

import Database.JDBC;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categoryItems;
    }

    @Override
    public void create(Category category) {

    }

    @Override
    public int update(Category category) {
        return 0;
    }

    @Override
    public int delete(Category category) {
        return 0;
    }

    @Override
    public Category selectedById(int id) {
        return null;
    }

    @Override
    public List<Category> selectByCondition(String condition) {
        return List.of();
    }
}
