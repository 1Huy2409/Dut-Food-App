//package Controller.Client.Payment;
//
//import DAO.*;
//import Helper.AlertMessage;
//import Helper.UserSession;
//import Model.*;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
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
//    private RadioButton zalopay;
//    @FXML
//    private RadioButton momo;
//    @FXML
//    private RadioButton cash;
//    @FXML
//    private RadioButton credit_card;
//    private ToggleGroup paymentGroup;
//    private List<CartItem> checkedItems;
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//         renderCheckedProducts();
//        paymentGroup = new ToggleGroup();
//        cash.setToggleGroup(paymentGroup);
//        credit_card.setToggleGroup(paymentGroup);
//        momo.setToggleGroup(paymentGroup);
//        zalopay.setToggleGroup(paymentGroup);
//    }
//    private String getSelectedPaymentMethod() {
//        RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();
//        if (selected == cash) return "cash";
//        if (selected == credit_card) return "credit_card";
//        if (selected == momo) return "momo";
//        if (selected == zalopay) return "zalopay";
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
//            AlertMessage.showAlertSuccessMessage("Order successful, thank you!");
//
//            if (onCheckoutSuccess != null) {
//                onCheckoutSuccess.run();
//            }
//        }
//    }
//    private Runnable onCheckoutSuccess;
//    public void setOnCheckoutSuccess(Runnable callback) {
//        this.onCheckoutSuccess = callback;
//    }
//}
