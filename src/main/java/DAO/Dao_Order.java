package DAO;

import Database.JDBC;
import Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Order implements Dao_Interface<Order>{
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("cartId"),
                        rs.getDouble("totalPrice"),
                        rs.getString("status"),
                        rs.getString("orderDate")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public int create(Order order) {
        String sql = "INSERT INTO orders (userId, cartId, totalPrice, status, orderDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getCartId());
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getStatus());
            pstmt.setString(5, order.getOrderDate());

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
    public int update(Order order) {
        String sql = "UPDATE orders SET userId = ?, cartId = ?, totalPrice = ?, status = ?, orderDate = ? WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getCartId());
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getStatus());
            pstmt.setString(5, order.getOrderDate());
            pstmt.setInt(6, order.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Order order) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getId());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Order selectedById(Order order) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("cartId"),
                        rs.getDouble("totalPrice"),
                        rs.getString("status"),
                        rs.getString("orderDate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> selectByCondition(String condition) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("cartId"),
                        rs.getDouble("totalPrice"),
                        rs.getString("status"),
                        rs.getString("orderDate")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
