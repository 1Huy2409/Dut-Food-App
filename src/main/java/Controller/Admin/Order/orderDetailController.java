package Controller.Admin.Order;

import DAO.Dao_Food;
import DAO.Dao_OrderItems;
import Model.FoodItem;
import Model.Order;
import Model.OrderItem;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderDetailController {
    private List<OrderItem> orderItemList;
    @FXML
    private VBox OrderBox;
    private Map<Integer, FoodItem> FoodItemMap = new HashMap<>();
    @FXML
    public void initialize() {
        OrderBox.setSpacing(10);
        // Initialization logic can go here
        // load ra tất cả orderitems trong order đã chọn
        loadOrderItems(orderController.orderSelected);
    }
    private void loadOrderItems(Order order) {
        // Logic to load order items for the selected order
        // This could involve fetching data from a database or service
        orderItemList = Dao_OrderItems.getInstance().getOrderItemsByOrderId(order.getId());
        loadFoodItemMap();
        if (orderItemList != null && !orderItemList.isEmpty()) {
            // Process the order items as needed, e.g., display them in a table or list
            for (OrderItem item : orderItemList) {
                System.out.println("Order Item: " + item.getFoodItemId() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice());
                HBox orderItemBox = new HBox();
                orderItemBox.setAlignment(Pos.CENTER_LEFT);
                orderItemBox.setSpacing(30);
                FoodItem foodItem = FoodItemMap.get(item.getFoodItemId());
                if (foodItem != null) {
                    VBox foodItemBox = new VBox();
                    foodItemBox.setPrefWidth(100);
                    foodItemBox.setPrefHeight(100);
                    File file = new File("src/main/resources/" + foodItem.getImageUrl());
                    Image image = new Image(file.toURI().toString());
                    ImageView imgFood = new ImageView();
                    imgFood.setImage(image);
                    imgFood.setFitWidth(80);
                    imgFood.setFitHeight(80);
                    Rectangle clip = new Rectangle(imgFood.getFitWidth(), imgFood.getFitHeight());
                    clip.setArcHeight(10);
                    clip.setArcWidth(10);
                    imgFood.setClip(clip);
                    Label foodNameLabel = new Label(foodItem.getFoodName());
                    foodItemBox.getChildren().add(imgFood);
                    foodItemBox.getChildren().add(foodNameLabel);
                    Label foodPriceLabel = new Label("Price: " + String.format("%,.0f VND", foodItem.getPrice()));
                    Label quantityLabel = new Label("Quantity: " + item.getQuantity());
                    Label priceLabel = new Label("Total Price: " + String.format("%,.0f VND", item.getPrice()));
                    // add vào orderItemBox
                    orderItemBox.getChildren().addAll(foodItemBox, foodPriceLabel ,quantityLabel, priceLabel);
                    OrderBox.getChildren().add(orderItemBox);
                } else {
                    System.out.println("Food item not found for ID: " + item.getFoodItemId());
                }
            }
        } else {
            System.out.println("No order items found for this order.");
        }
    }
    private void loadFoodItemMap()
    {
        List<FoodItem> foodItems = Dao_Food.getInstance().getAll();
        for (FoodItem foodItem : foodItems) {
            FoodItemMap.put(foodItem.getId(), foodItem);
        }
    }
}
