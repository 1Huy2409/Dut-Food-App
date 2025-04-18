package Controller.Client.Account;

import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.io.IOException;
import DAO.Dao_Cart;
import DAO.Dao_CartItem;
import DAO.Dao_Food;
import Model.Cart;
import Model.CartItem;
import Model.Category;
import Helper.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.application.Platform;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class cartController implements Initializable {

    @FXML
    private Button applyCouponBtn;

    @FXML
    private HBox cartItemTemplate;

    @FXML
    private TextField couponCodeTextField;

    @FXML
    private Button proceedToCheckoutBtn;

    @FXML
    private CheckBox productCheckbox;

    @FXML
    private TilePane productContainer;

    @FXML
    private Label productDesc;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private TextField productQuantity;

    @FXML
    private Label subtotal;

    @FXML
    private Label total;
    @FXML
    private Button minusButton;

    @FXML
    private Button plusButton;

    @FXML
    private Label stockLimitLabel;

    private double totalPrice;
    private List<CartItem> cartItemsChecked = new ArrayList<>();

    // create list cartChecked => then set on action push to order list

    public void initialize(URL location, ResourceBundle resources)
    {
        renderCart();
    }

    private HBox cloneTemplate() {
        try {
            HBox item = FXMLLoader.load(getClass().getResource("/View/Client/cart_item_template.fxml"));

            // DEBUG: Kiểm tra các thành phần
            System.out.println("Tìm thấy productName? " + (item.lookup("#productName") != null));
            System.out.println("Tìm thấy productImage? " + (item.lookup("#productImage") != null));

            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return new HBox(new Label("Lỗi load template")); // Fallback UI
        }
    }
    public void renderCart()
    {
        total.setText("0.0");
        Cart cart = Dao_Cart.getInstance().selectedByUserId(UserSession.getInstance().getId());
        List<CartItem> cartItems = Dao_CartItem.getInstance().selectedByIdCart(cart.getId());
        productContainer.getChildren().clear();
        for (CartItem item : cartItems)
        {
            String foodName = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getFoodName();
            String foodDesc = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getDescription();
            Double foodPrice = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice();
            int stock = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock();
            HBox clonedItem = cloneTemplate();

            CheckBox checkBox = (CheckBox) clonedItem.lookup("#productCheckbox");
            checkBox.setUserData(item);
            ImageView imageView = (ImageView) clonedItem.lookup("#productImage");
            Label nameLabel = (Label) clonedItem.lookup("#productName");
            Label descLabel = (Label) clonedItem.lookup("#productDesc");
            Label priceLabel = (Label) clonedItem.lookup("#productPrice");
            TextField quantityTextfield = (TextField) clonedItem.lookup("#productQuantity");
            Button minusButton = (Button) clonedItem.lookup("#minusButton");
            Button plusButton = (Button) clonedItem.lookup("#plusButton");
            ImageView btnDel = (ImageView) clonedItem.lookup("#deleteIcon");

            nameLabel.setText(foodName);
            descLabel.setText(foodDesc);
            priceLabel.setText(String.format("%,.0f VND", foodPrice));
            quantityTextfield.setText(String.format("%,d", item.getQuantity()));
            // chi cho phep nhap so
            quantityTextfield.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[0-9]*")) {
                    return change;
                }
                return null;
            }));
            // imageView.setImage(new Image(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl()));
            String dbPath = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl();
            String correctedPath = dbPath.startsWith("/") ? dbPath : "/" + dbPath;

            Image image = new Image(getClass().getResource(correctedPath).toString());
            imageView.setImage(image);
            imageView.setFitWidth(120);
            imageView.setFitHeight(90);
            imageView.setPreserveRatio(false);
            Rectangle clip = new Rectangle(120, 90);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);

            minusButton.setOnAction(e -> {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity > 0 && newQuantity <= stock) {
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                stockLimitLabel.setVisible(false);
                renderTotalPrice();
            });

            plusButton.setOnAction(e -> {
                int newQuantity = item.getQuantity() + 1;
                if (newQuantity <= stock) {
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                else
                {
                    newQuantity = stock;
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                stockLimitLabel.setVisible(newQuantity == stock);
                renderTotalPrice();
            });

            quantityTextfield.setOnAction(e -> {
                try {
                    int newQuantity = Integer.parseInt(quantityTextfield.getText());

                    int currentStock = stock;

                    if (newQuantity > currentStock) {
                        newQuantity = currentStock;
                        quantityTextfield.setText(String.valueOf(currentStock));
                    }

                    if (newQuantity > 0) {
                        item.setQuantity(newQuantity);
                        Dao_CartItem.getInstance().update(item);
                    } else {
                        quantityTextfield.setText(String.valueOf(item.getQuantity()));
                    }
                    stockLimitLabel.setVisible(newQuantity == currentStock);

                } catch (NumberFormatException ex) {
                    quantityTextfield.setText(String.valueOf(item.getQuantity()));
                }
            });

            checkBox.setOnAction(e -> {
                CheckBox source = (CheckBox) e.getSource();
                CartItem checkedItem = (CartItem) source.getUserData();

                if (source.isSelected()) {
                    // tinh tien vao total price
                    this.cartItemsChecked.add(checkedItem);
                    for (CartItem ct : this.cartItemsChecked)
                    {
                        System.out.print(ct.getFoodItemId() + ", ");
                    }
                    System.out.println();
                    renderTotalPrice();
                    System.out.println("da chon cartitem co id: " + checkedItem.getId());
                } else {
                    // bo mon do ra khoi thi tru ra khoi total price
                    this.cartItemsChecked.remove(checkedItem);
                    for (CartItem ct : this.cartItemsChecked)
                    {
                        System.out.print(ct.getFoodItemId() + ", ");
                    }
                    System.out.println();
                    renderTotalPrice();
                    System.out.println("da bo chon cartitem co id: " + checkedItem.getId());
                }
            });

            // bắt sự kiện cho btnDel
            btnDel.setOnMouseClicked(event ->
            {
                int index = this.cartItemsChecked.indexOf(item);
                if (index != -1) // không tìm thấy trong danh sách đã check
                {

                    this.cartItemsChecked.remove(item);
                }
                // xóa thẳng ra khỏi database
                Dao_CartItem.getInstance().delete(item);

                // xóa item này ra khỏi giao diện cart
            });

            productContainer.getChildren().add(clonedItem);
        }
    }
    public void renderTotalPrice()
    {
        this.totalPrice = 0.0;
        for (CartItem item: this.cartItemsChecked)
        {
            double priceEachItem = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice();
            this.totalPrice += priceEachItem * item.getQuantity();
        }
        total.setText(Double.toString(this.totalPrice));
    }
}
