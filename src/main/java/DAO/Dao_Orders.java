package DAO;

import Config.JDBC;
import Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Orders implements Dao_Interface<Order> {
    public static Dao_Orders getInstance() {
        return new Dao_Orders();
    }

    @Override
    public void create(Order order) {
        int generatedId = -1;
        String query = "INSERT INTO orders (user_id, cart_id, total_price, status) VALUES (?, ?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getCartId());
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getStatus() != null ? order.getStatus() : "Pending");

            int res = pstmt.executeUpdate();
            System.out.println("New order created.");
            System.out.println("Rows affected: " + res);

            if (res > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    order.setId(generatedId);
                }
                rs.close();
            }
            pstmt.close();
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY order_date DESC";

        try {
            Connection con = JDBC.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCartId(rs.getInt("cart_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                orderList.add(order);
            }

            rs.close();
            stmt.close();
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }

    @Override
    public int update(Order order) {
        String query = "UPDATE orders SET user_id = ?, cart_id = ?, total_price = ?, status = ? WHERE id = ?";

        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getCartId());
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getStatus());
            pstmt.setInt(5, order.getId());

            int rs = pstmt.executeUpdate();
            System.out.println("Order updated: " + rs);

            pstmt.close();
            JDBC.closeConnection(con);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateStatus(Order order) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";

        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, order.getStatus());
            pstmt.setInt(2, order.getId());

            int rs = pstmt.executeUpdate();
            System.out.println("Order status updated: " + rs);

            pstmt.close();
            JDBC.closeConnection(con);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Order order) {
        String query = "DELETE FROM orders WHERE id = ?";

        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, order.getId());

            int rs = pstmt.executeUpdate();
            System.out.println("Order deleted: " + rs);

            pstmt.close();
            JDBC.closeConnection(con);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order selectedById(int id) {
        String query = "select * from orders where id = ?";

        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCartId(rs.getInt("cart_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getTimestamp("order_date"));

                rs.close();
                pstmt.close();
                JDBC.closeConnection(con);
                return order;
            }

            rs.close();
            pstmt.close();
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // không tìm thấy
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";

        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCartId(rs.getInt("cart_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                orderList.add(order);
            }

            rs.close();
            pstmt.close();
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }

    @Override
    public List<Order> selectByCondition(String condition) {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE " + condition + " ORDER BY order_date DESC";

        try {
            Connection con = JDBC.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setCartId(rs.getInt("cart_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                orderList.add(order);
            }

            rs.close();
            stmt.close();
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderList;
    }
}