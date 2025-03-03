package DAO;

import Database.JDBC;
import Model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_CartItem implements Dao_Interface<CartItem>{
    @Override
    public List<CartItem> getAll() {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_items";

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CartItem cartItem = new CartItem(
                        rs.getInt("id"),
                        rs.getInt("cartId"),
                        rs.getInt("foodItemId"),
                        rs.getInt("quantity"),
                        rs.getString("addedAt")
                );
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public int create(CartItem cartItem) {
        String sql = "INSERT INTO cart_items (cartId, foodItemId, quantity, addedAt) VALUES (?, ?, ?, ?)";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, cartItem.getCartId());
            pstmt.setInt(2, cartItem.getFoodItemId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.setString(4, cartItem.getAddedAt());

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
    public int update(CartItem cartItem) {
        String sql = "UPDATE cart_items SET cartId = ?, foodItemId = ?, quantity = ?, addedAt = ? WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartItem.getCartId());
            pstmt.setInt(2, cartItem.getFoodItemId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.setString(4, cartItem.getAddedAt());
            pstmt.setInt(5, cartItem.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(CartItem cartItem) {
        String sql = "DELETE FROM cart_items WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartItem.getId());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public CartItem selectedById(CartItem cartItem) {
        String sql = "SELECT * FROM cart_items WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartItem.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new CartItem(
                        rs.getInt("id"),
                        rs.getInt("cartId"),
                        rs.getInt("foodItemId"),
                        rs.getInt("quantity"),
                        rs.getString("addedAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CartItem> selectByCondition(String condition) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_items WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CartItem cartItem = new CartItem(
                        rs.getInt("id"),
                        rs.getInt("cartId"),
                        rs.getInt("foodItemId"),
                        rs.getInt("quantity"),
                        rs.getString("addedAt")
                );
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }
}
