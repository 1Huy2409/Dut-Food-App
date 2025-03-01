package Model;

import java.sql.Timestamp;

public class CartItems {
    private int id;
    private int cartId;
    private int foodItemId;
    private int quantity;
    private Timestamp addedAt;
    // constructor
    public CartItems()
    {

    }
    public CartItems(int id, int cartId, int foodItemId, int quantity, Timestamp addedAt) {
        this.id = id;
        this.cartId = cartId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }
    // getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
}
