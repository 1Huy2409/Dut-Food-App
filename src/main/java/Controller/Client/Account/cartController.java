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
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;

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
    private Label productQuantity;

    @FXML
    private Label subtotal;

    @FXML
    private Label total;

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
            ImageView imageView = (ImageView) clonedItem.lookup("#productImage");
            Label nameLabel = (Label) clonedItem.lookup("#productName");
            Label descLabel = (Label) clonedItem.lookup("#productDesc");
            Label priceLabel = (Label) clonedItem.lookup("#productPrice");
            Label quantityLabel = (Label) clonedItem.lookup("#productQuantity");

            nameLabel.setText(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getFoodName());
            descLabel.setText(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getDescription());
            priceLabel.setText(String.format("%,.0f VND", Dao_Food.getInstance().selectedById(item.getFoodItemId()).getPrice()));
            quantityLabel.setText(String.format("%,d", Dao_CartItem.getInstance().selectedById(item.getId()).getQuantity()));
//            imageView.setImage(new Image(Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl()));
            String dbPath = Dao_Food.getInstance().selectedById(item.getFoodItemId()).getImageUrl();
            String correctedPath = dbPath.startsWith("/") ? dbPath : "/" + dbPath;

            Image image = new Image(getClass().getResource(correctedPath).toString());
            imageView.setImage(image);

            productContainer.getChildren().add(clonedItem);
        }

    }
}
