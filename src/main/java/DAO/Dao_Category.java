package DAO;

import Database.JDBC;
import Model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Category implements Dao_Interface<Category>{
    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("categoryName"),
                        rs.getString("description"),
                        rs.getString("createdAt")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public int create(Category category) {
        String sql = "INSERT INTO categories (categoryName, description, createdAt) VALUES (?, ?, ?)";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setString(3, category.getCreatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về ID tự sinh
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Category category) {
        String sql = "UPDATE categories SET categoryName = ?, description = ?, createdAt = ? WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setString(3, category.getCreatedAt());
            pstmt.setInt(4, category.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Category category) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, category.getId());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Category selectedById(int id) {
//        String sql = "SELECT * FROM categories WHERE id = ?";
//
//        try (Connection conn = JDBC.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, category.getId());
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                return new Category(
//                        rs.getInt("id"),
//                        rs.getString("categoryName"),
//                        rs.getString("description"),
//                        rs.getString("createdAt")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return null;
    }
    @Override
    public List<Category> selectByCondition(String condition) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("categoryName"),
                        rs.getString("description"),
                        rs.getString("createdAt")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
