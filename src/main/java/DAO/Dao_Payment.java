package DAO;

import Database.JDBC;
import Model.Payment;

import java.sql.Connection;
import java.sql.Date;
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
        return 0;
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
