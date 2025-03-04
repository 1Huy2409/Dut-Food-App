package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

=======
>>>>>>> phihihi
public class Order {
    private int id;
    private int userId;
    private int cartId;
    private double totalPrice;
    private String status;
<<<<<<< HEAD
    private Timestamp orderDate;

    // constructor
    public Order()
    {

    }
    public Order(int id, int userId, int cartId, double totalPrice, String status, Timestamp orderDate) {
=======
    private String orderDate;

    public Order(int id, int userId, int cartId, double totalPrice, String status, String orderDate) {
>>>>>>> phihihi
        this.id = id;
        this.userId = userId;
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }
<<<<<<< HEAD
    // getter and setter
=======

>>>>>>> phihihi
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

<<<<<<< HEAD
    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
=======
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
>>>>>>> phihihi
        this.orderDate = orderDate;
    }
}
