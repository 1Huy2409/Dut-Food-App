package Controller.Client.Account;

import javafx.fxml.FXMLLoader;
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



import java.net.URL;
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
        Cart cart = Dao_Cart.getInstance().selectedByUserId(UserSession.getInstance().getId());
        List<CartItem> cartItems = Dao_CartItem.getInstance().selectedByIdCart(cart.getId());
        productContainer.getChildren().clear();
        for (CartItem item : cartItems)
        {
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

            nameLabel.setText(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getFoodName());
            descLabel.setText(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getDescription());
            priceLabel.setText(String.format("%,.0f VND", Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice()));
            quantityTextfield.setText(String.format("%,d", Dao_CartItem.getInstance().selectedById(item.getId()).getQuantity()));
            // chi cho phep nhap so
            quantityTextfield.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[0-9]*")) {
                    return change;
                }
                return null;
            }));
//            imageView.setImage(new Image(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl()));
            String dbPath = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl();
            String correctedPath = dbPath.startsWith("/") ? dbPath : "/" + dbPath;

            Image image = new Image(getClass().getResource(correctedPath).toString());
            imageView.setImage(image);

            minusButton.setOnAction(e -> {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity > 0 && newQuantity <= Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock()) {
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                stockLimitLabel.setVisible(false);
            });

            plusButton.setOnAction(e -> {
                int newQuantity = item.getQuantity() + 1;
                if (newQuantity <= Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock()) {
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                else
                {
                    newQuantity = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock();
                    item.setQuantity(newQuantity);
                    Dao_CartItem.getInstance().update(item);
                    quantityTextfield.setText(String.valueOf(newQuantity));
                }
                stockLimitLabel.setVisible(newQuantity == Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock());
            });

            quantityTextfield.setOnAction(e -> {
                try {
                    int newQuantity = Integer.parseInt(quantityTextfield.getText());

                    int stock = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getStock();

                    if (newQuantity > stock) {
                        newQuantity = stock;
                        quantityTextfield.setText(String.valueOf(stock));
                    }

                    if (newQuantity > 0) {
                        item.setQuantity(newQuantity);
                        Dao_CartItem.getInstance().update(item);
                    } else {
                        quantityTextfield.setText(String.valueOf(item.getQuantity()));
                    }
                    stockLimitLabel.setVisible(newQuantity == stock);

                } catch (NumberFormatException ex) {
                    quantityTextfield.setText(String.valueOf(item.getQuantity()));
                }
            });

            checkBox.setOnAction(e -> {
                CheckBox source = (CheckBox) e.getSource();
                CartItem checkedItem = (CartItem) source.getUserData();

                if (source.isSelected()) {
                    System.out.println("da chon cartitem co id: " + checkedItem.getId());
                } else {
                    System.out.println("da bo chon cartitem co id: " + checkedItem.getId());
                }
            });


            productContainer.getChildren().add(clonedItem);
        }

    }

}
