package DAO;

import DAO.Dao_Interface;
import Database.JDBC;
import Model.CartItem;
import Model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao_CartItem implements Dao_Interface<CartItem> {
    public static Dao_CartItem getInstance()
    {
        return new Dao_CartItem();
    }
    @Override
    public List<CartItem> getAll()
    {
        List<CartItem> cart = new ArrayList<>();
        String query = "select * from cartitems";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int cart_id = rs.getInt("cart_id");
                int foodItem_id = rs.getInt("foodItem_id");
                int quantity = rs.getInt("quantity");
                Timestamp addedAt = rs.getTimestamp("added_at");
                CartItem item = new CartItem(id, cart_id, foodItem_id, quantity, addedAt);
                cart.add(item);
            }
            JDBC.closeConnection(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cart;
    }
    @Override
    public void create(CartItem cartItem)
    {
        String query = "insert into cartitems (cart_id, foodItem_id, quantity)" +
                "values (?, ?, ?)";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cartItem.getCartId());
            pstmt.setInt(2, cartItem.getFoodItemId());
            pstmt.setInt(3, cartItem.getQuantity());
            int res = pstmt.executeUpdate(); // return rows have been changed;
            System.out.println("You have created new cartItem!");
            System.out.println("Rows have been changed: " + res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int update(CartItem cartItem)
    {
        String query = "update cartitems set foodItem_id = ?, quantity = ? where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cartItem.getFoodItemId());
            pstmt.setInt(2, cartItem.getQuantity());
            pstmt.setInt(3, cartItem.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public int delete(CartItem cartItem)
    {
        String query = "delete from cartitems where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cartItem.getId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public CartItem selectedById(int id)
    {
        CartItem item = new CartItem();
        String query = "select * from cartitems where id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                item.setId(rs.getInt("id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setFoodItemId(rs.getInt("foodItem_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setAddedAt(rs.getTimestamp("added_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }
    @Override
    public List<CartItem> selectByCondition(String condition)
    {
        List<CartItem> cart = new ArrayList<>();
        try {
            Connection con = JDBC.getConnection();
            String query = "select * from cartitems ";
            switch (condition)
            {
                case "categoriesBtn":
//                    query += "order by sold desc limit 2";
                    break;
                default:
                    break;
            }
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int cart_id = rs.getInt("cart_id");
                int foodItem_id = rs.getInt("foodItem_id");
                int quantity = rs.getInt("quantity");
                Timestamp addedAt = rs.getTimestamp("added_at");
                CartItem item = new CartItem(id, cart_id, foodItem_id, quantity, addedAt);
                cart.add(item);
            }
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }
    public List<CartItem> selectedByIdCart(int idCart)
    {
        List<CartItem> cart = new ArrayList<>();
        String query = "select * from cartitems where cart_id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, idCart);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int cart_id = rs.getInt("cart_id");
                int foodItem_id = rs.getInt("foodItem_id");
                int quantity = rs.getInt("quantity");
                Timestamp addedAt = rs.getTimestamp("added_at");
                CartItem item = new CartItem(id, cart_id, foodItem_id, quantity, addedAt);
                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }
    public CartItem checkItemExist(int cartId, int foodItemId)
    {
        String query = "select * from cartitems where cart_id = ? and foodItem_id = ?";
        try {

            int count = 0;
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cartId);
            pstmt.setInt(2, foodItemId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                int cart_id = rs.getInt("cart_id");
                int foodItem_id = rs.getInt("foodItem_id");
                int quantity = rs.getInt("quantity");
                Timestamp added_at = rs.getTimestamp("added_at");
                CartItem findItem = new CartItem(id, cart_id, foodItem_id, quantity, added_at);
                return findItem;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    // update quantity in cart
    public void updateQuantity(CartItem cartItem)
    {
        String query = "update cartitems set quantity = ? where cart_id = ? and foodItem_id = ?";
        try {
            Connection con = JDBC.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, cartItem.getQuantity());
            pstmt.setInt(2, cartItem.getCartId());
            pstmt.setInt(3, cartItem.getFoodItemId());
            int rs = pstmt.executeUpdate();
            System.out.println("Rows have been changed: " + rs);
            JDBC.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return;
    }
}