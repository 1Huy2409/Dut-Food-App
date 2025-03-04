package DAO;

import Database.JDBC;
import Model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Payment implements Dao_Interface<Payment>{
    @Override
    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getString("paymentDate")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public int create(Payment payment) {
        String sql = "INSERT INTO payments (orderId, paymentMethod, amount, status, paymentDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, payment.getOrderId());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());
            pstmt.setString(5, payment.getPaymentDate());

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
    public int update(Payment payment) {
        String sql = "UPDATE payments SET orderId = ?, paymentMethod = ?, amount = ?, status = ?, paymentDate = ? WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, payment.getOrderId());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());
            pstmt.setString(5, payment.getPaymentDate());
            pstmt.setInt(6, payment.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Payment payment) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, payment.getId());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Payment selectedById(Payment payment) {
        String sql = "SELECT * FROM payments WHERE id = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, payment.getId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getString("paymentDate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> selectByCondition(String condition) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE " + condition;

        try (Connection conn = JDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getString("paymentDate")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
}
