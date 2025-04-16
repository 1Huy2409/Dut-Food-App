package DAO;

import Database.JDBC;
import Model.Cart;
import Model.Category;
import Model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_Cart implements Dao_Interface<Cart> {
    public static Dao_Cart getInstance()
    {
        return new Dao_Cart();
    }
    @Override
    public List<Cart> getAll()
    {
        List<Cart> cartItems = new ArrayList<>();
        String query = "select * from carts";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Cart item = new Cart(userID);
                cartItems.add(item);
            }
            JDBC.closeConnection(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cartItems;
    }
    @Override
    public void create(Cart cart)
    {
        String query = "insert into carts (user_id)" +
                "values (?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cart.getUserId());
            int res = pstmt.executeUpdate(); // return rows have been changed;
            System.out.println("You have created new cart!");
            System.out.println("Rows have been changed: " + res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int update(Cart cart)
    {
        String query = "update carts set user_id = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cart.getUserId());
            pstmt.setInt(2, cart.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public int delete(Cart cart)
    {
        String query = "delete from carts where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cart.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public Cart selectedById(int id)
    {
        Cart item = new Cart(id);
        String query = "select * from carts where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }
    @Override
    public List<Cart> selectByCondition(String condition)
    {
        List<Cart> cartItems = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from carts ";
            switch (condition)
            {
                case "byUserId":
//                    query += "where user_id = ";
                    break;
                default:
                    break;
            }
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Cart item = new Cart(user_id);
                cartItems.add(item);
            }
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cartItems;
    }
    public Cart selectedByUserId(int id)
    {
        Cart item = new Cart(id);
        String query = "select * from carts where user_id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }
}