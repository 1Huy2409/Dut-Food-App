package Model;

import java.sql.Timestamp;

public class FoodItem {
    private int id;
    private String foodName;
    private String description;
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
        this.createdAt = createdAt;
    }
}