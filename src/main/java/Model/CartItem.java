package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

=======
>>>>>>> phihihi
public class CartItem {
    private int id;
    private int cartId;
    private int foodItemId;
    private int quantity;
<<<<<<< HEAD
    private Timestamp addedAt;
    // constructor
    public CartItem()
    {

    }
    public CartItem(int id, int cartId, int foodItemId, int quantity, Timestamp addedAt) {
=======
    private String addedAt;

    public CartItem(int id, int cartId, int foodItemId, int quantity, String addedAt) {
>>>>>>> phihihi
        this.id = id;
        this.cartId = cartId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.addedAt = addedAt;
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

<<<<<<< HEAD
    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
=======
    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
>>>>>>> phihihi
        this.addedAt = addedAt;
    }
}
