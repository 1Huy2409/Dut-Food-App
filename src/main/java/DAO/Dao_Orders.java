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
        return 0;
    }

    @Override
    public int delete(Order order) {
        return 0;
    }

    @Override
    public Order selectedById(int id) {
        return null;
    }

    @Override
    public List<Order> selectByCondition(String condition) {
        return List.of();
    }
}
