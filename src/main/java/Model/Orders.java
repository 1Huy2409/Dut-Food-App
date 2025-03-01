package Model;

import java.sql.Timestamp;

public class Orders {
    private int id;
    private int userId;
    private int cartId;
    private double totalPrice;
    private String status;
    private Timestamp orderDate;

    // constructor
    public Orders()
    {

    }
    public Orders(int id, int userId, int cartId, double totalPrice, String status, Timestamp orderDate) {
        this.id = id;
        this.userId = userId;
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }
    // getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}
