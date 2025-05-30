package DAO;

import Config.JDBC;
import Model.OrderInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Dao_OrderInfo implements Dao_Interface<OrderInfo>{
    private static Dao_OrderInfo _instance;
    public static Dao_OrderInfo getInstance() {
        if (_instance == null) {
            _instance = new Dao_OrderInfo();
        }
        return _instance;
    }
    @Override
    public List<OrderInfo> getAll() {
        return List.of();
    }

    @Override
    public void create(OrderInfo orderInfo) {
        String query = "insert into OrderInfo (order_id, fullname, phone, address) " +
                       "values (?, ?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, orderInfo.getOrder_id());
            pstmt.setString(2, orderInfo.getFullname());
            pstmt.setString(3, orderInfo.getPhone());
            pstmt.setString(4, orderInfo.getAddress());
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int update(OrderInfo orderInfo) {
        return 0;
    }

    @Override
    public int delete(OrderInfo orderInfo) {
        return 0;
    }

    @Override
    public OrderInfo selectedById(int id) {
        return null;
    }

    @Override
    public List<OrderInfo> selectByCondition(String condition) {
        return List.of();
    }
    public OrderInfo getOrderInfoByOrderId(int orderId) {
        String query = "SELECT * FROM OrderInfo WHERE order_id = ?";
        try (Connection con = JDBC.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new OrderInfo(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("fullname"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
