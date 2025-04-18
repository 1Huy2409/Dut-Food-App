package DAO;

import Database.JDBC;
import Model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Dao_OrderItems implements Dao_Interface<OrderItem>{
    public static Dao_OrderItems getInstance()
    {
        return new Dao_OrderItems();
    }

    @Override
    public List<OrderItem> getAll() {
        return List.of();
    }

    @Override
    public void create(OrderItem orderItem) {
        String query = "insert into orderitems (order_id, foodItem_id, quantity, price) VALUES (?, ?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getFoodItemId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setDouble(4, orderItem.getPrice());
            int res = pstmt.executeUpdate();
            System.out.println("New order item added.");
            System.out.println("Rows affected: " + res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(OrderItem orderItem) {
        return 0;
    }

    @Override
    public int delete(OrderItem orderItem) {
        return 0;
    }

    @Override
    public OrderItem selectedById(int id) {
        return null;
    }

    @Override
    public List<OrderItem> selectByCondition(String condition) {
        return List.of();
    }
}
