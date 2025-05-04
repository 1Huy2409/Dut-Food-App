//
//package Controller.Client.Payment;
//
//import DAO.*;
//import Helper.AlertMessage;
//import Helper.UserSession;
//import Model.*;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.TilePane;
//import javafx.scene.layout.VBox;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class paymentController implements Initializable {
//    @FXML
//    private VBox productListContainer; // Container chứa sản phẩm
//    @FXML
//    private TilePane paymentItemContainer;
//    @FXML
//    private Label address;
//    @FXML
//    private Label lbtien;
//
//    @FXML
//    private RadioButton vnpay;
//    @FXML
//    private RadioButton cash;
//    private ToggleGroup paymentGroup;
//    private List<CartItem> checkedItems;
//
//    private VBox contentArea;
//    private void loadUI(String fxml)
//    {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
//            Parent root = loader.load();
//            this.contentArea.getChildren().clear();
//            this.contentArea.getChildren().add(root);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public void setContentArea(VBox contentArea) {
//        this.contentArea = contentArea;
//    }
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // renderCheckedProducts();
//        paymentGroup = new ToggleGroup();
//        cash.setToggleGroup(paymentGroup);
//        vnpay.setToggleGroup(paymentGroup);
//    }
//    private String getSelectedPaymentMethod() {
//        RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();
//        if (selected == cash) return "Cash";
//
//        if (selected == vnpay) return "VNPay";
//        return null;
//    }
//
//    public void setCheckedItems(List<CartItem> checkedItems) {
//        this.checkedItems = checkedItems;
//        renderCheckedProducts();
//    }
//    private void renderCheckedProducts() {
//        paymentItemContainer.getChildren().clear();
//
//        for (CartItem item : checkedItems) {
//            try {
//                HBox itemBox = FXMLLoader.load(getClass().getResource("/View/Client/cart_item_template.fxml"));
//                Label name = (Label) itemBox.lookup("#productName");
//                Label desc = (Label) itemBox.lookup("#productDesc");
//                Label price = (Label) itemBox.lookup("#productPrice");
//                TextField quantity = (TextField) itemBox.lookup("#productQuantity");
//                ImageView img = (ImageView) itemBox.lookup("#productImage");
//                CheckBox checkbox = (CheckBox) itemBox.lookup("#productCheckbox");
//                Button minus = (Button) itemBox.lookup("#minusButton");
//                Button plus = (Button) itemBox.lookup("#plusButton");
//                ImageView deleteIcon = (ImageView) itemBox.lookup("#deleteIcon");
//
//                // Ẩn các thành phần không cần thiết
//                checkbox.setVisible(false);
//                minus.setVisible(false);
//                plus.setVisible(false);
//                deleteIcon.setVisible(false);
//                quantity.setEditable(false);
//
//                // Gán thông tin
//                FoodItem food = Dao_Food.getInstance().selectedById(item.getFoodItemId());
//                name.setText(food.getFoodName());
//                desc.setText(food.getDescription());
//                price.setText(String.format("%,.0f VND", food.getPrice()));
//                quantity.setText(String.valueOf(item.getQuantity()));
//
//                String imgPath = food.getImageUrl();
//                Image image = new Image(getClass().getResource(imgPath.startsWith("/") ? imgPath : "/" + imgPath).toString());
//                img.setImage(image);
//
//                paymentItemContainer.getChildren().add(itemBox);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void setAddress(String address) {
//        this.address.setText(address);
//    }
//    public void setPrice(double price) {
//        this.lbtien.setText("Thành Tiền: " + Double.toString(price) + " VND");
//    }
//    public void acceptPay() {
//        if (getSelectedPaymentMethod() == null) {
//            AlertMessage.showAlertErrorMessage("Please select payment method");
//        } else {
//            Order order = new Order();
//            order.setUserId(UserSession.getInstance().getId());
//            order.setCartId(UserSession.getInstance().getCartId());
//            order.setTotalPrice(Double.parseDouble(lbtien.getText().replaceAll("[^\\d.]", "")));
//            Dao_Orders.getInstance().create(order);
//
//
//            for (CartItem item : checkedItems) {
//                FoodItem foodItem = new FoodItem();
//                foodItem = Dao_Food.getInstance().selectedById(item.getFoodItemId());
//                foodItem.setStock(foodItem.getStock() - item.getQuantity());
//                Dao_Food.getInstance().update(foodItem);
//                OrderItem orderItem = new OrderItem();
//                orderItem.setOrderId(order.getId());
//                orderItem.setFoodItemId(item.getFoodItemId());
//                orderItem.setQuantity(item.getQuantity());
//                orderItem.setPrice(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice() * item.getQuantity());
//                Dao_OrderItems.getInstance().create(orderItem);
//                Dao_CartItem.getInstance().delete(item);
//            }
//
//            Payment payment = new Payment();
//            payment.setOrderId(order.getId());
//            payment.setPaymentMethod(getSelectedPaymentMethod());
//            payment.setAmount(order.getTotalPrice());
//            Dao_Payment.getInstance().create(payment);
//
//            AlertMessage.showAlertSuccessMessage("Order successful, thank you!");
//            if (onCheckoutSuccess != null) {
//                onCheckoutSuccess.run();
//            }
//            if (payment.getPaymentMethod().equals("Cash"))
//            {
//                // load ra giao dien home
//                loadUI("/View/Client/category.fxml");
//            }
//            else
//            {
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/vnpay.fxml"));
//                    Parent root = loader.load();
//                    vnpayController vnpaycontroller = loader.getController();
//                    vnpaycontroller.setAmount((int)Double.parseDouble(lbtien.getText().replaceAll("[^\\d.]", "")));
//                    vnpaycontroller.setOrderId(Integer.toString(order.getId()));
//                    this.contentArea.getChildren().clear();
//                    this.contentArea.getChildren().add(root);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }
//    }
//    private Runnable onCheckoutSuccess;
//    public void setOnCheckoutSuccess(Runnable callback) {
//        this.onCheckoutSuccess = callback;
//    }
//}
package Controller.Client.Payment;

import Controller.Client.Account.cartController;
import Controller.Client.Product.categoryController;
import DAO.*;
import DAO.Dao_Food;
import Model.CartItem;
import Model.FoodItem;
import Model.*;
import Helper.UserSession;
import Helper.AlertMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class paymentController implements Initializable {

    @FXML
    private TilePane paymentItemContainer;

    @FXML
    private VBox productListContainer;
    @FXML
    private Button btnVnpay;
    @FXML
    private Button btnCash;
    @FXML
    private Label lbTotal;
    @FXML
    private Label lbAddress;
    @FXML
    private VBox paymentMethodArea;
    private double totalPrice;
    private double totalPrice1;
    private Button lastSelectedButton;
    private List<CartItem> checkedItems;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnVnpay.getStyleClass().add("selected-category");
        lastSelectedButton = btnVnpay;
        loadUI("/View/Client/vnpay.fxml");
    }

    private void loadUI(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            switch (fxml)
            {
                case "/View/Client/vnpay.fxml":
                    vnpayController controller = loader.getController();
                    controller.setContentArea(paymentMethodArea);

                    break;
//                case "/View/Client/cart.fxml":
//                    cartController cartCtrl = loader.getController();
//                    cartCtrl.setContentArea(paymentMethodArea);
//                    break;
//                case "/View/Client/category.fxml":
//                    categoryController controller = loader.getController();
//                    controller.setContentArea(contentArea);
            }
//            detailProductController controller = loader.getController();
//            controller.setContentArea(contentArea);

            // 🔑 Cho phép root giãn chiều cao trong VBox
            VBox.setVgrow(root, Priority.ALWAYS);
            paymentMethodArea.getChildren().clear();
            paymentMethodArea.getChildren().add(root);
            if (root instanceof Region) {
                Region region = (Region) root;
                region.prefWidthProperty().bind(paymentMethodArea.widthProperty());
                region.prefHeightProperty().bind(paymentMethodArea.heightProperty());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void setContentArea(VBox contentArea) {
        this.paymentMethodArea = contentArea;
    }
        public void setAddress(String address) {
        this.lbAddress.setText(address);
    }
    public void setCheckedItems(List<CartItem> checkedItems) {
        this.checkedItems = checkedItems;
        renderCheckedProducts();
        renderTotalPrice();
    }

    private void renderCheckedProducts() {
        paymentItemContainer.getChildren().clear();

        for (CartItem item : checkedItems) {
            try {
                HBox itemBox = FXMLLoader.load(getClass().getResource("/View/Client/cart_item_template.fxml"));
                Label name = (Label) itemBox.lookup("#productName");
                Label desc = (Label) itemBox.lookup("#productDesc");
                Label price = (Label) itemBox.lookup("#productPrice");
                TextField quantity = (TextField) itemBox.lookup("#productQuantity");
                ImageView img = (ImageView) itemBox.lookup("#productImage");
                CheckBox checkbox = (CheckBox) itemBox.lookup("#productCheckbox");
                Button minus = (Button) itemBox.lookup("#minusButton");
                Button plus = (Button) itemBox.lookup("#plusButton");
                ImageView deleteIcon = (ImageView) itemBox.lookup("#deleteIcon");

                // Ẩn các thành phần không cần thiết
                checkbox.setVisible(false);
                minus.setVisible(false);
                plus.setVisible(false);
                deleteIcon.setVisible(false);
                quantity.setEditable(false);

                // Gán thông tin
                FoodItem food = Dao_Food.getInstance().selectedById(item.getFoodItemId());
                name.setText(food.getFoodName());
                desc.setText(food.getDescription());
                price.setText(String.format("%,.0f VND", food.getPrice()));
                quantity.setText(String.valueOf(item.getQuantity()));

                String imgPath = food.getImageUrl();
                Image image = new Image(getClass().getResource(imgPath.startsWith("/") ? imgPath : "/" + imgPath).toString());
                img.setImage(image);

                paymentItemContainer.getChildren().add(itemBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPrice(double price) {
        this.lbTotal.setText("Thành Tiền: " + Double.toString(price) + " VND");
    }

    public void acceptPay() {
        Order order = new Order();
        order.setUserId(UserSession.getInstance().getId());
        order.setCartId(UserSession.getInstance().getCartId());
        order.setTotalPrice(Double.parseDouble(lbTotal.getText().replaceAll("[^\\d.]", "")));
        Dao_Orders.getInstance().create(order);


        for (CartItem item : checkedItems) {
            FoodItem foodItem = new FoodItem();
            foodItem = Dao_Food.getInstance().selectedById(item.getFoodItemId());
            foodItem.setStock(foodItem.getStock() - item.getQuantity());
            Dao_Food.getInstance().update(foodItem);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setFoodItemId(item.getFoodItemId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice() * item.getQuantity());
            Dao_OrderItems.getInstance().create(orderItem);
            Dao_CartItem.getInstance().delete(item);
        }

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
//            payment.setPaymentMethod(getSelectedPaymentMethod());
        payment.setAmount(order.getTotalPrice());
        Dao_Payment.getInstance().create(payment);
        AlertMessage.showAlertSuccessMessage("Order successful, thank you!");

        if (onCheckoutSuccess != null) {
            onCheckoutSuccess.run();
        }
    }
    private Runnable onCheckoutSuccess;
    public void setOnCheckoutSuccess(Runnable callback) {
        this.onCheckoutSuccess = callback;
    }
    public void renderTotalPrice()
    {
        this.totalPrice = 0.0;
        for (CartItem item: this.checkedItems)
        {
            double priceEachItem = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice();
            this.totalPrice += priceEachItem * item.getQuantity();
        }
        lbTotal.setText(Double.toString(this.totalPrice));
    }
    public double priceChecked()
    {
        this.totalPrice1 = 0.0;
        for (CartItem item: this.checkedItems)
        {
            double priceEachItem = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice();
            this.totalPrice1 += priceEachItem * item.getQuantity();
        }
        return totalPrice1;
    }
    public void VNPAYOnAction(MouseEvent e)
    {
        lastSelectedButton.getStyleClass().remove("selected-category");
        lastSelectedButton = btnVnpay;
        lastSelectedButton.getStyleClass().add("selected-category");
        loadUI("/View/Client/vnpay.fxml");

//        btnCategory.getStyleClass().add("selected-button-container");
    }
    public void CASHOnAction(MouseEvent e)
    {
        lastSelectedButton.getStyleClass().remove("selected-category");
        lastSelectedButton = btnCash;
        lastSelectedButton.getStyleClass().add("selected-category");
//        loadUI("/View/Client/vnpay.fxml");
//        btnCategory.getStyleClass().add("selected-button-container");
    }
}
