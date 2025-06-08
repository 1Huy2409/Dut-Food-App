package Controller.Client.Order;

import Controller.Client.clientController;
import DAO.*;
import Helper.AlertMessage;
import Helper.InvoiceGenerator;
import Helper.RouteScreen;
import Helper.UserSession;
import Model.*;
import com.mysql.cj.Session;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderController {
    @FXML
    private VBox OrdersPage;
    private List<OrderItem> orderItemList;
    private List<Order> orderList;

    private Map<Integer, FoodItem> FoodItemMap = new HashMap<>();

    public void initialize() {
        OrdersPage.setSpacing(10);
        orderList = Dao_Orders.getInstance().getOrdersByUserId(UserSession.getInstance().getId());
        for(Order order : orderList){
            loadOrderItems(order);
        }

    }
    private void loadOrderItems(Order order) {
        orderItemList = Dao_OrderItems.getInstance().getOrderItemsByOrderId(order.getId());
        loadFoodItemMap();

        if (orderItemList != null && !orderItemList.isEmpty()) {
            VBox orderContainer = new VBox();
            orderContainer.setSpacing(10);
            orderContainer.setAlignment(Pos.CENTER);

            for (OrderItem item : orderItemList) {
                HBox orderItemBox = new HBox();
                orderItemBox.setAlignment(Pos.CENTER);
                orderItemBox.setSpacing(30);

                FoodItem foodItem = FoodItemMap.get(item.getFoodItemId());

                if (foodItem != null) {
                    VBox foodItemBox = new VBox();
                    foodItemBox.setPrefWidth(100);
                    foodItemBox.setPrefHeight(100);
                    foodItemBox.setAlignment(Pos.CENTER);

                    File file = new File("src/main/resources/" + foodItem.getImageUrl());
                    Image image = new Image(file.toURI().toString());
                    ImageView imgFood = new ImageView(image);
                    imgFood.setFitWidth(80);
                    imgFood.setFitHeight(80);

                    Rectangle clip = new Rectangle(imgFood.getFitWidth(), imgFood.getFitHeight());
                    clip.setArcHeight(10);
                    clip.setArcWidth(10);
                    imgFood.setClip(clip);

                    Label foodNameLabel = new Label(foodItem.getFoodName());

                    foodItemBox.getChildren().addAll(imgFood, foodNameLabel);

                    Label foodPriceLabel = new Label("Giá: " + String.format("%,.0f VND", foodItem.getPrice()));
                    Label quantityLabel = new Label("Số lượng: " + item.getQuantity());
                    Label priceLabel = new Label("Tổng: " + String.format("%,.0f VND", item.getPrice()));

                    orderItemBox.getChildren().addAll(foodItemBox, foodPriceLabel, quantityLabel, priceLabel);
                    orderContainer.getChildren().add(orderItemBox);
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = order.getOrderDate().toLocalDateTime().format(formatter);
            Label orderDateLabel = new Label("Ngày mua: " + formattedDate);
            orderDateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444; -fx-font-weight: bold;");

            HBox dateBox = new HBox(orderDateLabel);
            dateBox.setAlignment(Pos.CENTER);
            orderContainer.getChildren().add(dateBox);

            Separator separator = new Separator();
            orderContainer.getChildren().add(separator);

            OrdersPage.getChildren().add(orderContainer);
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
