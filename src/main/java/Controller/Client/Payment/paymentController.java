package Controller.Client.Payment;

import DAO.Dao_Food;
import DAO.Dao_OrderItems;
import DAO.Dao_Orders;
import DAO.Dao_Payment;
import Helper.AlertMessage;
import Helper.UserSession;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class paymentController implements Initializable {
//    private List<CartItem> selectedItems;
//    private Runnable onCancel;
//    public void setSelectedItems(List<CartItem> items) {
//        this.selectedItems = items;
//        //renderItems();
//    }
//    public void setOnCancel(Runnable onCancel) {
//        this.onCancel = onCancel;
//    }
    @FXML
    private VBox productListContainer; // Container chứa sản phẩm
    @FXML
    private TilePane paymentItemContainer;
    @FXML
    private Label address;
    @FXML
    private Label lbtien;
    private List<CartItem> checkedItems;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // renderCheckedProducts();
    }
    public void setCheckedItems(List<CartItem> checkedItems) {
        this.checkedItems = checkedItems;
        renderCheckedProducts();
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
    public void setAddress(String address) {
        this.address.setText(address);
    }
    public void setPrice(double price) {
        this.lbtien.setText(Double.toString(price));
    }
    public void acceptPay(){
        Order order = new Order();
        order.setUserId(UserSession.getInstance().getId());
        order.setCartId(UserSession.getInstance().getCartId());
        order.setTotalPrice(Double.parseDouble(lbtien.getText()));
        Dao_Orders.getInstance().create(order);


        for(CartItem item : checkedItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setFoodItemId(item.getFoodItemId());
            orderItem.setQuantity(1);
            orderItem.setPrice(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice());
            Dao_OrderItems.getInstance().create(orderItem);
        }
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setPaymentMethod("momo");
        payment.setAmount(order.getTotalPrice());
        Dao_Payment.getInstance().create(payment);
        AlertMessage.showAlertSuccessMessage("Dat hang thanh cong");
    }
}
