package DAO;

import Database.JDBC;
import Model.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Cart implements Dao_Interface<Cart>{

    @Override
    public List<Cart> getAll() {
        List<Cart> cartList = new ArrayList<>();
        String query = "SELECT * FROM cart";

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("createdAt")
                );
                cartList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartList;
    }

    @Override
    public int create(Cart cart) {
        String query = "INSERT INTO cart (userId, createdAt) VALUES (?, ?)";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, cart.getUserId());
            pstmt.setString(2, cart.getCreatedAt());

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
    public int update(Cart cart) {
        String query = "UPDATE cart SET userId = ?, createdAt = ? WHERE id = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, cart.getUserId());
            pstmt.setString(2, cart.getCreatedAt());
            pstmt.setInt(3, cart.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Cart cart) {
        String query = "DELETE FROM cart WHERE id = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, cart.getId());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Cart selectedById(Cart cart) {
        String query = "SELECT * FROM cart WHERE id = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, cart.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Cart(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("createdAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cart> selectByCondition(String condition) {
        List<Cart> cartList = new ArrayList<>();
        String query = "SELECT * FROM cart WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("createdAt")
                );
                cartList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartList;
    }
}
