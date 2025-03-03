package DAO;

import Database.JDBC;
import Model.Role;
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
        return List.of();
    }

    @Override
    public int create(Role role) {
        return 0;
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
    public Role selectedById(Role role){
        return  null;
    }
    @Override
    public List<Role> selectByCondition(String condition){
        return List.of();
    }
}
