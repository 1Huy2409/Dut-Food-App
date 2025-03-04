package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

=======
>>>>>>> phihihi
public class FoodItem {
    private int id;
    private String foodName;
    private String description;
<<<<<<< HEAD
    private Double price;
    private int stock;
    private int categoryId;
    private String imageUrl;
    private boolean status;
    private Timestamp createdAt;
    private int sold;
    // constructor 1: default set status
    public FoodItem()
    {
        this.status = true;
        // auto created_at current time
    }
    // constructor 2: full field

    public FoodItem(int id, String foodName, String description, Double price, int stock, int categoryId, String imageUrl, boolean status, Timestamp createdAt, int sold) {
        this.id = id;
        this.foodName = foodName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.sold = sold;
=======
    private double price;
    private int categoryId;
    private String imageUrl;
    private int status;
    private String createdAt;

    public FoodItem(int id, String foodName, String description, double price,int categoryId, String imageUrl, int status, String createdAt) {
        this.id = id;
        this.foodName = foodName;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
>>>>>>> phihihi
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

<<<<<<< HEAD
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

=======
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

>>>>>>> phihihi
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

<<<<<<< HEAD
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
=======
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
>>>>>>> phihihi
        this.createdAt = createdAt;
    }
}
