package DAO;

import Config.JDBC;
import Model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Dao_Payment implements Dao_Interface<Payment>{
    public static Dao_Payment getInstance()
    {
        return new Dao_Payment();
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    @Override
    public void create(Payment payment) {
        String query = "insert into payments (order_id, payment_method, amount) VALUES (?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmount());
            int res = pstmt.executeUpdate();
            System.out.println("Payment recorded.");
            System.out.println("Rows affected: " + res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Payment payment) {
        String query = "update payment set status = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1,"completed");
            pstmt.setInt(2, payment.getId());

            int rs = pstmt.executeUpdate();
            System.out.println("Payment status updated: " + rs);
            JDBC.closeConnection(con);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Payment payment) {
        return 0;
    }

    @Override
    public Payment selectedById(int id) {
        return null;
    }

    @Override
    public List<Payment> selectByCondition(String condition) {
        return List.of();
    }
}
