
package Controller.Client.Payment;

import DAO.*;
import Helper.AlertMessage;
import Helper.UserSession;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class paymentController implements Initializable {
    @FXML
    private VBox productListContainer; // Container chứa sản phẩm
    @FXML
    private TilePane paymentItemContainer;
    @FXML
    private Label address;
    @FXML
    private Label lbtien;

    @FXML
    private RadioButton vnpay;
    @FXML
    private RadioButton cash;
    private ToggleGroup paymentGroup;
    private List<CartItem> checkedItems;
    private OrderInfo newOrderInfo;
    private VBox contentArea;
    private void loadUI(String fxml)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            this.contentArea.getChildren().clear();
            this.contentArea.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setContentArea(VBox contentArea) {
        this.contentArea = contentArea;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // renderCheckedProducts();
        paymentGroup = new ToggleGroup();
        cash.setToggleGroup(paymentGroup);
        vnpay.setToggleGroup(paymentGroup);
    }
    private String getSelectedPaymentMethod() {
        RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();
        if (selected == cash) return "Cash";

        if (selected == vnpay) return "VNPay";
        return null;
    }

    public void setCheckedItems(List<CartItem> checkedItems) {
        this.checkedItems = checkedItems;
        renderCheckedProducts();
    }
    private void renderCheckedProducts() {
        paymentItemContainer.getChildren().clear();

        for (CartItem item : checkedItems) {
            try {
                HBox itemBox = FXMLLoader.load(getClass().getResource("/View/Client/Order/oder_item.fxml"));
                Label name = (Label) itemBox.lookup("#productName");
                Label price = (Label) itemBox.lookup("#productPrice");
                Label quantity = (Label) itemBox.lookup("#quantity");
                ImageView img = (ImageView) itemBox.lookup("#productImage");
                FoodItem food = Dao_Food.getInstance().selectedById(item.getFoodItemId());
                name.setText(food.getFoodName());
                price.setText(String.format("%,.0f VND", food.getPrice()*item.getQuantity()));
                quantity.setText(String.format("x%d", item.getQuantity()));


                String imgPath = food.getImageUrl();
                Image image = new Image(getClass().getResource(imgPath.startsWith("/") ? imgPath : "/" + imgPath).toString());
                img.setImage(image);
                img.setFitWidth(120);
                img.setFitHeight(90);
                img.setPreserveRatio(false);
                Rectangle clip = new Rectangle(120, 90);
                clip.setArcWidth(20);
                clip.setArcHeight(20);

                img.setClip(clip);
                img.setTranslateX(10);

                paymentItemContainer.getChildren().add(itemBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setAddress(OrderInfo orderInfo) {
        this.newOrderInfo = orderInfo;
        this.address.setText(orderInfo.getFullname() + ", " + orderInfo.getPhone() + ", " + orderInfo.getAddress());
    }
    public void setPrice(double price) {
        this.lbtien.setText("Thành Tiền: " + String.format("%,.0f VND", price));
    }
    public void acceptPay() {
        if (getSelectedPaymentMethod() == null) {
            AlertMessage.showAlertErrorMessage("Please select payment method");
        } else {
            Order order = new Order();
            order.setUserId(UserSession.getInstance().getId());
            order.setCartId(UserSession.getInstance().getCartId());
            order.setTotalPrice(Double.parseDouble(lbtien.getText().replaceAll("[^\\d.]", "")));
            Dao_Orders.getInstance().create(order);


            for (CartItem item : checkedItems) {
                FoodItem foodItem = new FoodItem();
                foodItem = Dao_Food.getInstance().selectedById(item.getFoodItemId());
                foodItem.setStock(foodItem.getStock() - item.getQuantity());
                foodItem.setSold(foodItem.getSold() + item.getQuantity());
                Dao_Food.getInstance().update(foodItem);
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setFoodItemId(item.getFoodItemId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice() * item.getQuantity());
                Dao_OrderItems.getInstance().create(orderItem);
                Dao_CartItem.getInstance().delete(item);
            }
            newOrderInfo.setOrder_id(order.getId());
            Dao_OrderInfo.getInstance().create(newOrderInfo);
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setPaymentMethod(getSelectedPaymentMethod());
            payment.setAmount(order.getTotalPrice());
            Dao_Payment.getInstance().create(payment);

            AlertMessage.showAlertSuccessMessage("Order successful, thank you!");
            if (onCheckoutSuccess != null) {
                onCheckoutSuccess.run();
            }
            if (payment.getPaymentMethod().equals("Cash"))
            {
                loadUI("/View/Client/Product/product.fxml");
            }
            else
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Payment/vnpay.fxml"));
                    Parent root = loader.load();
                    vnpayController vnpaycontroller = loader.getController();
                    vnpaycontroller.setAmount((int)Double.parseDouble(lbtien.getText().replaceAll("[^\\d.]", "")));
                    vnpaycontroller.setOrderId(Integer.toString(order.getId()));
                    this.contentArea.getChildren().clear();
                    this.contentArea.getChildren().add(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    private Runnable onCheckoutSuccess;
    public void setOnCheckoutSuccess(Runnable callback) {
        this.onCheckoutSuccess = callback;
    }
}
