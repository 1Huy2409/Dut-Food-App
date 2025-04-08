package DAO;

import Database.JDBC;
import Model.ForgotPassword;

import java.lang.constant.Constable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Dao_ForgotPassword implements Dao_Interface<ForgotPassword> {
    public static Dao_ForgotPassword getInstance()
    {
        return new Dao_ForgotPassword();
    }
    @Override
    public List<ForgotPassword> getAll() {
        return List.of();
    }

    @Override
    public void create(ForgotPassword forgotPassword) {
        String query = "insert into forgotPassword (email, otp) values (?,?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, forgotPassword.getEmail());
            pstmt.setString(2, forgotPassword.getOtp());
            int rs = pstmt.executeUpdate();
            if (rs > 0)
            {
                System.out.println("You have changed: " + rs + " rows!");
            }
            else
            {
                System.out.println("Cannot create new records");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(ForgotPassword forgotPassword) {
        return 0;
    }

    @Override
    public int delete(ForgotPassword forgotPassword) {
        return 0;
    }

    @Override
    public ForgotPassword selectedById(int id) {
        return null;
    }

    @Override
    public List<ForgotPassword> selectByCondition(String condition) {
        return List.of();
    }
    public boolean checkOtp(String email, String otp)
    {
        String query = "select count(id) from forgotPassword where email = ? and otp = ? limit 1";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, otp);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                if (rs.getInt(1) > 0)
                {
                    // exist this otp code for this email
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
