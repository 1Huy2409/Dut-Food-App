package DAO;

import Database.JDBC;
import Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Dao_User implements Dao_Interface<User> {
    // Declare all of methods be implemented from interface
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
                System.out.println(id + " - " + phone + " - " + fullName + " - " + email + " - " + userName + " - " + password + " - " + status + " - " + roleId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }
    // create new user and insert into user table
    @Override
    public int create(User user) {
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            // check email da ton tai hay chua

            String sql = "insert into users (fullName, email, userName, password, status)" +
                    " values ('"+user.getFullName()+"', '"+user.getEmail()+"', '"+user.getUserName()+"', '"+user.getPassWord()+"', "+user.getStatus()+")";
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
        return 0;
    }
    // find user and delete user in user table
    @Override
    public int delete(User user) {
        return 0;
    }

    @Override
    public User selectedById(User user) {
        return null;
    }

    @Override
    public List<User> selectByCondition(String condition) {
        return List.of();
    }
}
