package Model;

public class OrderItem {
    private int id;
    private int orderId;
    private int foodItemId;
    private int quantity;
    private double price;

<<<<<<< HEAD
    public OrderItem(){}

=======
>>>>>>> phihihi
    public OrderItem(int id, int orderId, int foodItemId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

<<<<<<< HEAD
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

=======
>>>>>>> phihihi
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
<<<<<<< HEAD
=======

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
>>>>>>> phihihi
}
