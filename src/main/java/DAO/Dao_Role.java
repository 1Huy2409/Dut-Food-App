package DAO;

import Config.JDBC;
import Model.Role;

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
        return List.of();
    }

    @Override
    public void create(Role role) {

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
        return List.of();
    }
}