package DAO;

import Database.JDBC;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_User implements Dao_Interface<User> {
    public static Dao_User getInstance()
    {
        return new Dao_User();
    }
    @Override
    // return all users in user table
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String sql = "select * from users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String phone = rs.getString("phone");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String userName = rs.getString("userName");
                String password = rs.getString("password");
                boolean status = rs.getBoolean("status");
                int roleId = rs.getInt("roleId");
                User user = new User(id,phone,fullName,email,userName,password,roleId,status);
                users.add(user);
//                System.out.println(id + " - " + phone + " - " + fullName + " - " + email + " - " + userName + " - " + password + " - " + status + " - " + roleId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
    // create new user and insert into user table
    @Override
    public int create(User user) {
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            // check email da ton tai hay chua

            String sql = "insert into users (phone, fullName, email, userName, password, status)" +
                    " values ('"+user.getPhone()+"', '"+user.getFullName()+"', '"+user.getEmail()+"', '"+user.getUserName()+"', '"+user.getPassWord()+"', "+user.getStatus()+")";
            int result = st.executeUpdate(sql);// return numbers of rows updated
            System.out.println("You executed: " + sql);
            System.out.println("Rows have been changed are: " + result);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    // find user need to be updated and update for this user
    @Override
    public int update(User user) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE users SET fullName = ?, email = ?, userName = ?, password = ?, status = ?, roleId = ?, phone = ? WHERE id = ?";
        try {
            con = JDBC.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getUserName());
            ps.setString(4, user.getPassWord());
            ps.setBoolean(5, user.getStatus());
            ps.setInt(6, user.getRoleId());
            ps.setString(7, user.getPhone());
            ps.setInt(8, user.getId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection(con);
        }

        return result;
    }
    // find user and delete user in user table
    @Override
    public int delete(User user) {
        String sql = "UPDATE users SET status = false WHERE id = ?";
        try (Connection con = JDBC.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public User selectedById(User user) {
        Connection con = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("fullName"));
                u.setEmail(rs.getString("email"));
                u.setUserName(rs.getString("userName"));
                u.setPassWord(rs.getString("password"));
                u.setRoleId(rs.getInt("roleId"));
                u.setPhone(rs.getString("phone"));
                u.setStatus(rs.getBoolean("status"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<User> selectByCondition(String condition) {
        List<User> users = new ArrayList<>();
        Connection con = JDBC.getConnection();
        String sql = "SELECT * FROM users WHERE " + condition;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("userName"));
                user.setPassWord(rs.getString("password"));
                user.setStatus(rs.getBoolean("status"));
                user.setRoleId(rs.getInt("roleId"));
                user.setPhone(rs.getString("phone"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

