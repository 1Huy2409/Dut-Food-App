package DAO;

import Database.JDBC;
import Model.Role;
<<<<<<< HEAD

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Dao_Role implements Dao_Interface<Role> {
    public static Dao_Role getInstance()
    {
        return new Dao_Role();
    }
    @Override
    public List<Role> getAll() {
=======
import Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Dao_Role implements Dao_Interface<Role>{
    @Override
    public List<Role> getAll(){
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection con = JDBC.getConnection();
            Statement st = con.createStatement();
            String sql = "select * from users";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String roleName = rs.getString("roleName");
                System.out.println(id + " - " + roleName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
>>>>>>> phihihi
        return List.of();
    }

    @Override
<<<<<<< HEAD
    public void create(Role role) {

=======
    public int create(Role role) {
        return 0;
>>>>>>> phihihi
    }

    @Override
    public int update(Role role) {
        return 0;
    }

    @Override
    public int delete(Role role) {
        return 0;
    }

    @Override
<<<<<<< HEAD
    public Role selectedById(int id) {
        Role itemRole = new Role();
        String query = "select * from roles where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int roleId = rs.getInt("id");
                String roleName = rs.getString("roleName");
                itemRole.setId(roleId);
                itemRole.setRoleName(roleName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemRole;
    }

    @Override
    public List<Role> selectByCondition(String condition) {
=======
    public Role selectedById(Role role){
        return  null;
    }
    @Override
    public List<Role> selectByCondition(String condition){
>>>>>>> phihihi
        return List.of();
    }
}
