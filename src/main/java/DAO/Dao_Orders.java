package DAO;

import Config.JDBC;
import Model.Order;

import java.sql.*;
import java.util.List;

public class Dao_Orders implements Dao_Interface<Order>{
    public static Dao_Orders getInstance()
    {
        return new Dao_Orders();
    }

    @Override
    public void create(Order order) {
        int generatedId = -1;
        String query = "insert into orders (user_id, cart_id, total_price) VALUES (?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getCartId());
            pstmt.setDouble(3, order.getTotalPrice());
            int res = pstmt.executeUpdate();
            System.out.println("New order created.");
            System.out.println("Rows affected: " + res);
            if (res > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    order.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }

    @Override
    public int update(Order order) {
        String query = "update orders set status = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, order.getStatus());
            pstmt.setInt(2, order.getId());

            int rs = pstmt.executeUpdate();
            System.out.println("Order status updated: " + rs);
            JDBC.closeConnection(con);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Order order) {
        return 0;
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
                JDBC.closeConnection(con);
                return order;
            }
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // không tìm thấy
    }

    @Override
    public List<Order> selectByCondition(String condition) {
        return List.of();
    }
}
