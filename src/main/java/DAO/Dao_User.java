package DAO;

import Config.JDBC;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Helper.*;

public class Dao_User implements Dao_Interface<User> {
    public static Dao_User getInstance() {
        return new Dao_User();
    }

    @Override
    // return all users in user table
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String sql = "select * from users where roleId = 2";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String phone = rs.getString("phone");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String userName = rs.getString("userName");
                String password = rs.getString("password");
                boolean status = rs.getBoolean("status");
                int roleId = rs.getInt("roleId");
                User user = new User(id, phone, fullName, email, userName, password, roleId, status);
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    // create new user and insert into user table
    @Override
    public void create(User user) {
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String sql = "insert into users (fullName, email, userName, password, status, roleId, phone)" +
                    " values ('" + user.getFullName() + "', '" + user.getEmail() + "', '" + user.getUserName() + "', '" + user.getPassWord() + "', " + user.getStatus() + ", " + user.getRoleId() + " , '" + user.getPhone() + "')";
            int result = st.executeUpdate(sql);
            System.out.println("You executed: " + sql);
            System.out.println("Rows have been changed are: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // find user need to be updated and update for this user

    @Override
    public int update(User user) {
        int result = 0;
        try {
            Connection con = JDBC.getConnection();
            String sql = "update users set fullName = ?, email = ?, phone = ?, image = ?, status = ? where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getImage());
            ps.setInt(6, user.getId());
            ps.setBoolean(5, user.getStatus());
            result = ps.executeUpdate();
            System.out.println("Rows updated: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int updatePassword(User user) {
        String query = "update users set password = ?, passwordResetOtp = null, passwordResetExpiration = null where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, user.getPassWord());
            pstmt.setInt(2, user.getId());
            int rs = pstmt.executeUpdate();
            if (rs > 0) {
                System.out.println("You have changed: " + rs + " rows");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int updateAccount(User user) {
        String query = "update users set password = ?, username = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, user.getPassWord());
            pstmt.setString(2, user.getUserName());
            pstmt.setInt(3, user.getId());
            int rs = pstmt.executeUpdate();
            if (rs > 0) {
                System.out.println("You have changed: " + rs + " rows");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    // find user and delete user in user table
    @Override
    public int delete(User user) {
        int result = 0;
        try {
            Connection con = JDBC.getConnection();
            String sql = "update users set status = false where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            result = ps.executeUpdate();
            System.out.println("Rows marked as deleted: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public User selectedById(int id) {
        User currentUser = null;
        try {
            Connection con = JDBC.getConnection();
            String sql = "select * from users where id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String phone = rs.getString("phone");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String userName = rs.getString("userName");
                String password = rs.getString("password");
                boolean status = rs.getBoolean("status");
                String img = rs.getString("image");
                int roleId = rs.getInt("roleId");

                currentUser = new User(id, phone, fullName, email, userName, password, roleId, status);
                currentUser.setImage(img);

                System.out.println(id + " - " + phone + " - " + fullName + " - " + email + " - " +
                        userName + " - " + password + " - " + status + " - " + roleId);
                JDBC.closeConnection(con);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currentUser;
    }


    @Override
    public List<User> selectByCondition(String condition) {
        return List.of();
    }

    public User checkLogin(String emailCheck, String passwordCheck) {
        User currentUser = null;
        try (Connection con = JDBC.getConnection()) {
            String query = "select * from users where email = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, emailCheck);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        // Check password with BCrypt
                        if (PasswordHelper.checkPassword(passwordCheck, hashedPassword)) {
                            currentUser = new User(rs.getInt("id"), rs.getString("phone"), rs.getString("fullName"), rs.getString("email"), rs.getString("userName"), rs.getString("password"), rs.getInt("roleId"), rs.getBoolean("status"));
                        } else {
                            AlertMessage.showAlertErrorMessage("Please enter correct password!");
                        }
                    } else {
                        AlertMessage.showAlertErrorMessage("This email doesn't exist. Please try again!");
                    }
                }
            }
        } catch (SQLException ex) {
            AlertMessage.showAlertErrorMessage("Database Connection Error: " + ex.getMessage());
        }
        return currentUser;
    }

    public User checkEmail(String email) {
        User currentUser = new User();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from users where email = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                currentUser.setId(rs.getInt("id"));
                currentUser.setFullName(rs.getString("fullName"));
                currentUser.setEmail(rs.getString("email"));
                currentUser.setUserName(rs.getString("userName"));
                currentUser.setPassWord(rs.getString("password"));
                currentUser.setStatus(rs.getBoolean("status"));
                currentUser.setRoleId(rs.getInt("roleId"));
                currentUser.setPhone(rs.getString("phone"));
                return currentUser;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public User checkRegister(String fullName, String email, String userName, String password, String phone) {
        try {
            Connection connection = JDBC.getConnection();

            // Check username
            String sqlUserName = "SELECT id FROM users WHERE userName = ?";
            PreparedStatement psUserName = connection.prepareStatement(sqlUserName);
            psUserName.setString(1, userName);
            ResultSet rsUserName = psUserName.executeQuery();
            if (rsUserName.next()) {
                AlertMessage.showAlertErrorMessage("Username is already taken!");
                return null;
            }

            // Check email
            String sqlEmail = "SELECT id FROM users WHERE email = ?";
            PreparedStatement psEmail = connection.prepareStatement(sqlEmail);
            psEmail.setString(1, email);
            ResultSet rsEmail = psEmail.executeQuery();
            if (rsEmail.next()) {
                AlertMessage.showAlertErrorMessage("Email is already registered!");
                return null;
            }

            // Check phone
            String sqlPhone = "SELECT id FROM users WHERE phone = ?";
            PreparedStatement psPhone = connection.prepareStatement(sqlPhone);
            psPhone.setString(1, phone);
            ResultSet rsPhone = psPhone.executeQuery();
            if (rsPhone.next()) {
                AlertMessage.showAlertErrorMessage("Phone number is already registered!");
                return null;
            }

            // Create new account
            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setUserName(userName);
            newUser.setPassWord(PasswordHelper.hashPassword(password));
            newUser.setRoleId(2);
            newUser.setPhone(phone);
            this.getInstance().create(newUser);
            AlertMessage.showAlertSuccessMessage("New account is created!");
            return newUser;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // method for forgot password
    public int updateForgotPassword(String email, String otp, Timestamp expiredAt) {
        try
        {
            Connection con = JDBC.getConnection();
            String query = "update users set passwordResetOtp = ?, passwordResetExpiration = ? where email = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, otp);
            pstmt.setTimestamp(2, expiredAt);
            pstmt.setString(3, email);
            int rs = pstmt.executeUpdate();
            if (rs > 0) {
                System.out.println("You have changed: " + rs + " rows!");
            } else {
                System.out.println("Cannot create new records");
            }
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkOtp(String email, String otp) {
        String query = "select count(id) from users where email = ? and passwordResetOtp = ? and passwordResetExpiration >= NOW() limit 1";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, otp);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
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
